package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.CreditRepository;
import com.cinovo.backend.DB.Util.Resolver.CreditResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.CreditType;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.cinovo.backend.TMDB.Response.CreditResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public List<Credit> findCreditByMediaIdAndType(final Integer media_id, final MediaType type) throws Exception
    {
        Optional<List<Credit>> credits = this.creditRepository.findCreditByMediaIdAndType(media_id);
        if(credits.isEmpty() || credits.get().isEmpty())
        {
            return this.onConvertTMDB(type + Shared.REGEX + media_id);
        }
        return credits.get();
    }

    public List<Credit> findCreditByPersonId(final Integer person_id) throws Exception
    {
        Optional<List<Credit>> credits = this.creditRepository.findCreditByPersonId(person_id);
        if(credits.isEmpty() || credits.get().isEmpty())
        {
            return this.onConvertTMDB(MediaType.PERSON + Shared.REGEX + person_id);
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
            Media media = !MediaType.valueOf(parts[0]).name().equals("PERSON") ? mediaService.getMediaByIdAndType(creditResponse.getId(),
                    MediaType.valueOf(parts[0])) : null;
            Person person = MediaType.valueOf(parts[0]).name().equals("PERSON") ? personService.findPersonById(creditResponse.getId()) : null;

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
        Person per = !MediaType.valueOf(parts[0]).name().equals("PERSON") ? this.personService.findPersonById(personId) : person;
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
            med = this.mediaService.getMediaByIdAndType(mediaResponse.getId(), type);
        }
        else
        {
            med = media;
        }

        return this.generateCredit(mediaResponse, med, per, creditType);

    }

    @Transactional
    private Credit generateCredit(final MediaResponse mediaResponse, final Media media, final Person person, final CreditType credit_type)
            throws Exception
    {
        if(mediaResponse.getId() == null || media == null)
            return new Credit();

        Credit credit = this.creditRepository.findCreditByMediaIdAndMediaTypeAndPersonCinevoId(media.getCinevo_id(), person.getCinevo_id())
                .orElse(new Credit());
        credit.setCredit_type(credit_type);
        credit.setCast_id(mediaResponse.getCast_id());
        credit.setCharacter(credit.getCharacter() != null && !credit.getCharacter().isBlank() ? credit.getCharacter() : mediaResponse.getCharacter());
        credit.setCredit_id(mediaResponse.getCredit_id());
        credit.setOrder(mediaResponse.getOrder());
        credit.setEpisode_count(mediaResponse.getEpisode_count());
        credit.setFirst_credit_air_date(mediaResponse.getFirst_air_date() != null && !mediaResponse.getFirst_air_date().isEmpty() ? LocalDate.parse(
                mediaResponse.getFirst_air_date()) : null);
        this.creditRepository.save(credit);

        //        List<String> departments = credit.getDepartment() != null ? new ArrayList<>(credit.getDepartment()) : new ArrayList<>();
        //        if(mediaResponse.getDepartment() != null && !departments.contains(mediaResponse.getDepartment()))
        //        {
        //            departments.add(mediaResponse.getDepartment());
        //        }
        //        credit.setDepartment(departments);
        //
        //        List<String> jobs = credit.getJob() != null ? new ArrayList<>(credit.getJob()) : new ArrayList<>();
        //        if(mediaResponse.getJob() != null && !jobs.contains(mediaResponse.getJob()))
        //        {
        //            jobs.add(mediaResponse.getJob());
        //        }
        //        credit.setJob(jobs);
        //
        //        if(mediaResponse.getRoles() != null)
        //        {
        //            List<Credit.Role> roles = new ArrayList<>();
        //            for(MediaResponse.Role role : mediaResponse.getRoles())
        //            {
        //                Credit.Role ro = this.creditRepository.findCreditRoleByCreditId(role.getCredit_id()).orElse(new Credit.Role());
        //                ro.setCredit_id(role.getCredit_id());
        //                ro.setCharacter(role.getCharacter());
        //                ro.setEpisode_count(role.getEpisode_count());
        //
        //                roles.add(ro);
        //            }
        //            credit.setRoles(roles);
        //        }
        //
        //        if(mediaResponse.getJobs() != null)
        //        {
        //            List<Credit.Job> jobList = new ArrayList<>();
        //            for(MediaResponse.Job job : mediaResponse.getJobs())
        //            {
        //                Credit.Job ro = this.creditRepository.findCreditJobByCreditId(job.getCredit_id()).orElse(new Credit.Job());
        //                ro.setCredit_id(job.getCredit_id());
        //                ro.setJob(job.getJob());
        //                ro.setEpisode_count(job.getEpisode_count());
        //
        //                jobList.add(ro);
        //            }
        //            credit.setJobs(jobList);
        //        }
        //
        //        if(person != null && media != null)
        //        {
        //            if(person.getMedias() == null)
        //            {
        //                person.setMedias(new ArrayList<>());
        //            }
        //            if(!person.getMedias().contains(media))
        //            {
        //                person.getMedias().add(media);
        //            }
        //
        //            if(!media.getCreated_by().contains(person))
        //            {
        //                media.getCreated_by().add(person);
        //            }
        //        }
        //
        //        credit.setMedia(media);
        //        credit.setPerson(person);
        //        this.creditRepository.save(credit);

        this.creditResolver.generateDateAsync(mediaResponse, credit, person, media);
        return credit;
    }
}
