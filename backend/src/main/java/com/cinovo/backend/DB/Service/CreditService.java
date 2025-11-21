package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.CreditRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.CreditType;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.cinovo.backend.TMDB.Response.CreditResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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

    public CreditService(CreditRepository creditRepository, @Lazy MediaService mediaService, PersonService personService,
            com.cinovo.backend.TMDB.Service service)
    {
        this.creditRepository = creditRepository;
        this.mediaService = mediaService;
        this.personService = personService;
        this.service = service;
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
        return this.onConvertTMDB(person_id + "PERSON");
    }

    @Override
    public List<Credit> onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);

        CreditResponse creditResponse = switch(MediaType.valueOf(parts[0]).name())
        {
            case "PERSON" ->
            {
                CreditResponse peopleCombinedCredit = this.service.getPeopleCombinedCredit(Integer.parseInt(parts[1]), "en-US");
                CreditResponse peopleMovie = this.service.getPeopleMovieCast(Integer.parseInt(parts[1]), "en-US");
                //Connect with TV not Movie to search
                CreditResponse peopleTv = new CreditResponse(0, new ArrayList<>(), new ArrayList<>());
                //                        this.service.getPeopleTvCast(Integer.parseInt(numbers), "en-US");

                List<MediaResponse> combinedCrew = new ArrayList<>();
                combinedCrew.addAll(peopleMovie.getCrew());
                combinedCrew.addAll(peopleCombinedCredit.getCrew());
                combinedCrew.addAll(peopleTv.getCrew());

                List<MediaResponse> combinedCast = new ArrayList<>();
                combinedCast.addAll(peopleMovie.getCast());
                combinedCast.addAll(peopleCombinedCredit.getCast());
                combinedCast.addAll(peopleTv.getCast());

                yield new CreditResponse(peopleMovie.getId(), combinedCrew, combinedCast);
            }
            case "MOVIE" -> this.service.getMovieCredit(Integer.parseInt(parts[1]), "en-US");
            case "TV" -> this.service.getTvAggregateCredits(Integer.parseInt(parts[1]), "en-US");
            default -> throw new IllegalArgumentException("Unknown type: " + obj);
        };

        Map<String, Credit> credits = new HashMap<>();

        if(creditResponse.getId() != null)
        {
            Media media = !MediaType.valueOf(parts[0]).name().equals("PERSON") ? mediaService.getMediaByIdAndType(creditResponse.getId(),
                    MediaType.valueOf(parts[0])) : null;
            Person person = !MediaType.valueOf(parts[0]).name().equals("PERSON") ? personService.findPersonById(creditResponse.getId()) : null;

            int count = 0;
            int max = 100;
            for(MediaResponse cast : creditResponse.getCast())
            {
                if(count == max)
                    break;
                count++;
                Person per = cast.getId() != null ? this.personService.findPersonById(cast.getId()) : person;
                Media med = cast.getId() != null ? this.mediaService.getMediaByIdAndType(creditResponse.getId(), MediaType.valueOf(parts[0])) : media;
                Credit credit = this.generateCredit(cast, med, per, CreditType.CAST);
                creditRepository.save(credit);
                credits.put(credit.getCinevo_id(), credit);
            }

            count = 0;
            for(MediaResponse crew : creditResponse.getCrew())
            {
                if(count == max)
                    break;
                count++;
                Person per = crew.getId() != null ? this.personService.findPersonById(crew.getId()) : person;
                Media med = crew.getId() != null ? this.mediaService.getMediaByIdAndType(creditResponse.getId(), MediaType.valueOf(parts[0])) : media;
                Credit credit = this.generateCredit(crew, med, per, CreditType.CREW);
                creditRepository.save(credit);
                credits.put(credit.getCinevo_id(), credit);
            }
        }

        return new ArrayList<>(credits.values());
    }

    private Credit generateCredit(final MediaResponse movieResponse, final Media media, final Person person, final CreditType credit_type)
    {
        if(movieResponse.getId() == null)
            return null;

        Credit credit = this.creditRepository.findCreditByMovieAndPerson(media != null ? media.getCinevo_id() : null,
                person != null ? person.getCinevo_id() : null).orElseGet(Credit::new);
        credit.setCredit_type(credit_type);
        credit.setCast_id(movieResponse.getCast_id());
        credit.setCharacter(credit.getCharacter() != null && !credit.getCharacter().isBlank() ? credit.getCharacter() : movieResponse.getCharacter());
        credit.setCredit_id(movieResponse.getCredit_id());
        credit.setOrder(movieResponse.getOrder());
        credit.setEpisode_count(movieResponse.getEpisode_count());
        credit.setFirst_credit_air_date(movieResponse.getFirst_air_date() != null && !movieResponse.getFirst_air_date().isEmpty() ? LocalDate.parse(
                movieResponse.getFirst_air_date()) : null);
        this.creditRepository.save(credit);

        List<String> departments = credit.getDepartment() != null ? new ArrayList<>(credit.getDepartment()) : new ArrayList<>();
        if(movieResponse.getDepartment() != null && !departments.contains(movieResponse.getDepartment()))
        {
            departments.add(movieResponse.getDepartment());
        }
        credit.setDepartment(departments);

        List<String> jobs = credit.getJob() != null ? new ArrayList<>(credit.getJob()) : new ArrayList<>();
        if(movieResponse.getJob() != null && !jobs.contains(movieResponse.getJob()))
        {
            jobs.add(movieResponse.getJob());
        }
        credit.setJob(jobs);

        if(movieResponse.getRoles() != null)
        {
            List<Credit.Role> roles = new ArrayList<>();
            for(MediaResponse.Role role : movieResponse.getRoles())
            {
                Credit.Role ro = this.creditRepository.findCreditRoleByCredit_Id(role.getCredit_id()).orElse(new Credit.Role());
                ro.setCredit_id(role.getCredit_id());
                ro.setCharacter(role.getCharacter());
                ro.setEpisode_count(role.getEpisode_count());

                roles.add(ro);
            }
            credit.setRoles(roles);
        }

        if(movieResponse.getJobs() != null)
        {
            List<Credit.Job> jobList = new ArrayList<>();
            for(MediaResponse.Job job : movieResponse.getJobs())
            {
                Credit.Job ro = this.creditRepository.findCreditJobByCredit_Id(job.getCredit_id()).orElse(new Credit.Job());
                ro.setCredit_id(job.getCredit_id());
                ro.setJob(job.getJob());
                ro.setEpisode_count(job.getEpisode_count());

                jobList.add(ro);
            }
            credit.setJobs(jobList);
        }
        credit.setMedia(media);
        credit.setPerson(person);

        return credit;
    }
}
