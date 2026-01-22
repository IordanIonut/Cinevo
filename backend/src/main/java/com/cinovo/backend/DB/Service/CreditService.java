package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Enum.CreditType;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.CreditRepository;
import com.cinovo.backend.DB.Util.Resolver.CreditResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.cinovo.backend.TMDB.Response.CreditResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@JBossLog
public class CreditService implements TMDBLogically<Object, List<Credit>>
{
    private final CreditRepository creditRepository;
    private final MediaService mediaService;
    private final PersonService personService;
    private final com.cinovo.backend.TMDB.Service service;
    private final CreditResolver creditResolver;

    public CreditService(CreditRepository creditRepository, @Lazy MediaService mediaService, PersonService personService, GenreService genreService,
            com.cinovo.backend.TMDB.Service service, CreditResolver creditResolver)
    {
        this.creditRepository = creditRepository;
        this.mediaService = mediaService;
        this.personService = personService;
        this.service = service;
        this.creditResolver = creditResolver;
    }

    public void updateOrInsertCreditDepartment(final String credit_cinevo_id, final String department)
    {
        this.creditRepository.updateOrInsertCreditDepartment(credit_cinevo_id, department);
    }

    public void updateOrInsertCreditPosition(final String credit_cinevo_id, final String position)
    {
        this.creditRepository.updateOrInsertCreditPosition(credit_cinevo_id, position);
    }

    public void updateOrInsertCreditRole(final String cinevo_id, final String credit_id, final String role, final Integer episode_count)
    {
        this.creditRepository.updateOrInsertCreditRole(cinevo_id, credit_id, role, episode_count);
    }

    public void updateOrInsertCreditRoleList(final String credit_cinevo_id, final String role_cinevo_id)
    {
        this.creditRepository.updateOrInsertCreditRoleList(credit_cinevo_id, role_cinevo_id);
    }

    public void updateOrInsertCreditJob(String cinevo_id, final String credit_id, final String job, final Integer episode_count)
    {
        this.creditRepository.updateOrInsertCreditJob(cinevo_id, credit_id, job, episode_count);
    }

    public void updateOrInsertCreditJobList(final String credit_cinevo_id, final String job_cinevo_id)
    {
        this.creditRepository.updateOrInsertCreditJobList(credit_cinevo_id, job_cinevo_id);
    }

    public Credit.Role findCreditRoleByCreditId(final String credit_id)
    {
        Optional<Credit.Role> creditRole = this.creditRepository.findCreditRoleByCreditId(credit_id);
        return creditRole.orElse(new Credit.Role());
    }

    public Credit.Job findCreditJobByCreditId(final String credit_id)
    {
        Optional<Credit.Job> creditJob = this.creditRepository.findCreditJobByCreditId(credit_id);
        return creditJob.orElse(new Credit.Job());
    }

    public Credit findCreditByCinevoId(final String cinevo_id)
    {
        Optional<Credit> credit = this.creditRepository.findCreditByCinevoId(cinevo_id);
        return credit.orElse(new Credit());
    }

    public Credit saveAndUpdate(final Credit credit)
    {
        this.creditRepository.save(credit);
        return credit;
    }

    public List<Credit> findByMediaTmdbId(final Integer media_tmdb_id, final MediaType type) throws Exception
    {
        Optional<List<Credit>> credits = this.creditRepository.findByMediaTmdbId(media_tmdb_id);
        if(credits.isEmpty() || credits.get().isEmpty())
        {
            return this.onConvertTMDB(type + Shared.REGEX + media_tmdb_id);
        }
        return credits.get();
    }

    public List<Credit> findByPersonTmdbId(final Integer person_tmdb_id) throws Exception
    {
        Optional<List<Credit>> credits = this.creditRepository.findByPersonTmdbId(person_tmdb_id);
        if(credits.isEmpty() || credits.get().isEmpty())
        {
            return this.onConvertTMDB(MediaType.PERSON + Shared.REGEX + person_tmdb_id);
        }
        return credits.get();
    }

    @Override
    public List<Credit> onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);

        CreditResponse creditResponse = switch(MediaType.valueOf(parts[0]).name())
        {
            case "PERSON" -> this.service.getPeopleCombinedCredit(Integer.parseInt(parts[1]), "en-US");
            case "MOVIE" -> this.service.getMovieCredit(Integer.parseInt(parts[1]), "en-US");
            case "TV" -> this.service.getTvAggregateCredits(Integer.parseInt(parts[1]), "en-US");
            default -> throw new IllegalArgumentException("Unknown type: " + obj);
        };

        Map<String, Credit> credits = new HashMap<>();

        if(creditResponse.getId() != null)
        {
            Media media = !MediaType.valueOf(parts[0]).name().equals("PERSON") ? mediaService.getMediaByTmdbIdAndMediaType(creditResponse.getId(),
                    MediaType.valueOf(parts[0])) : null;
            Person person = MediaType.valueOf(parts[0]).name().equals("PERSON") ? personService.findByTmdbId(creditResponse.getId()) : null;

            for(MediaResponse cast : creditResponse.getCast())
            {
                Credit credit = this.onRespectCondition(parts, person, media, cast, CreditType.CAST, cast.getId());
                if(credit.getCinevo_id() != null)
                {
                    credits.put(credit.getCinevo_id(), credit);
                }
            }

            for(MediaResponse crew : creditResponse.getCrew())
            {
                Credit credit = this.onRespectCondition(parts, person, media, crew, CreditType.CREW, crew.getId());
                if(credit.getCinevo_id() != null)
                {
                    credits.put(credit.getCinevo_id(), credit);
                }
            }
        }

        return new ArrayList<>(credits.values());
    }

    private Credit onRespectCondition(final String[] parts, final Person person, final Media media, final MediaResponse mediaResponse,
            final CreditType creditType, final Integer personId) throws Exception
    {
        Person per = !MediaType.valueOf(parts[0]).name().equals("PERSON") ? this.personService.findByTmdbId(personId) : person;
        Media med;
        if(MediaType.valueOf(parts[0]).name().equals("PERSON"))
        {
            MediaType type;
            if(mediaResponse.getMedia_type() != null)
            {
                type = MediaType.valueOf(mediaResponse.getMedia_type().toUpperCase());
            }
            else
            {
                type = this.mediaService.findFirstMediaTypeByName(mediaResponse.getTitle(), mediaResponse.getOriginal_title(), mediaResponse.getId());
            }
            med = this.mediaService.getMediaByTmdbIdAndMediaType(mediaResponse.getId(), type);
        }
        else
        {
            med = media;
        }

        return this.generateCredit(mediaResponse, med, per, creditType);

    }

    @Transactional
    protected Credit generateCredit(final MediaResponse mediaResponse, final Media media, final Person person, final CreditType credit_type)
    {
        if(mediaResponse.getId() == null || media == null)
        {
            return null;
        }
        String credit_cinevo_id = Shared.generateCinevoId(
                this.creditRepository.findCreditByMediaIdAndMediaTypeAndPersonCinevoId(media.getCinevo_id(), person.getCinevo_id()));

        this.creditRepository.updateOrInsertCredit(credit_cinevo_id, credit_type.name(), mediaResponse.getCast_id(), mediaResponse.getCharacter(),
                mediaResponse.getCredit_id(), mediaResponse.getOrder(), mediaResponse.getEpisode_count(),
                Shared.onStringParseDate(mediaResponse.getFirst_air_date()), media.getCinevo_id(), person.getCinevo_id());

        this.creditResolver.generateDateAsync(mediaResponse, credit_cinevo_id, person, media);
        return this.findCreditByCinevoId(credit_cinevo_id);
    }
}
