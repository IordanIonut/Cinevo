package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.CreditRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.CreditType;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.cinovo.backend.TMDB.Response.CreditResponse;
import lombok.extern.jbosslog.JBossLog;
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

    public CreditService(CreditRepository creditRepository, MediaService mediaService, PersonService personService,
            com.cinovo.backend.TMDB.Service service)
    {
        this.creditRepository = creditRepository;
        this.mediaService = mediaService;
        this.personService = personService;
        this.service = service;
    }

    public List<Credit> findCreditByMovieId(final Integer movie_id) throws Exception
    {
        Optional<List<Credit>> credits = this.creditRepository.findCreditByMovieId(movie_id);
        if(credits.isEmpty() || credits.get().isEmpty())
        {
            return this.onConvertTMDB(movie_id + "MOVIE");
        }
        return credits.get();
    }

    public List<Credit> findCreditByPersonId(final Integer person_id) throws Exception
    {
        return this.onConvertTMDB(person_id + "PERSON");
    }

    @Override
    public List<Credit> onConvertTMDB(Object input) throws Exception
    {
        String inputStr = String.valueOf(input);
        String numbers = inputStr.replaceAll("[^0-9]", "");
        String letters = inputStr.replaceAll("[^A-Za-z]", "");

        CreditResponse creditResponse = switch(letters)
        {
            case "PERSON" ->
            {
                CreditResponse peopleCombinedCredit = this.service.getPeopleCombinedCredit(Integer.parseInt(numbers), "en-US");
                CreditResponse peopleMovie = this.service.getPeopleMovieCast(Integer.parseInt(numbers), "en-US");
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
            case "MOVIE" -> this.service.getMovieCredit(Integer.parseInt(numbers), "en-US");
            default -> throw new IllegalArgumentException("Unknown type: " + letters);
        };

        Map<String, Credit> credits = new HashMap<>();
        Media media = letters.equals("MOVIE") ? mediaService.getMediaByIdAndType(creditResponse.getId(), MediaType.MOVIE) : null;
        Person person = letters.equals("PERSON") ? personService.findPersonById(creditResponse.getId()) : null;

        for(MediaResponse cast : creditResponse.getCast())
        {
            Person per = (media != null) ? this.personService.findPersonById(cast.getId()) : person;
            Media med = (per != null) ? this.mediaService.getMediaByIdAndType(cast.getId(), MediaType.MOVIE) : media;
            Credit credit = this.generateCredit(cast, med, per, CreditType.CAST);
            creditRepository.save(credit);
            credits.put(credit.getCinevo_id(), credit);
        }

        for(MediaResponse crew : creditResponse.getCrew())
        {
            Person per = (media != null) ? this.personService.findPersonById(crew.getId()) : person;
            Media med = (per != null) ? this.mediaService.getMediaByIdAndType(crew.getId(), MediaType.MOVIE) : media;
            Credit credit = this.generateCredit(crew, med, per, CreditType.CREW);
            creditRepository.save(credit);
            credits.put(credit.getCinevo_id(), credit);
        }

        return new ArrayList<>(credits.values());
    }

    private Credit generateCredit(final MediaResponse movieResponse, final Media media, final Person person, final CreditType credit_type)
    {
        Credit credit = this.creditRepository.findCreditByMovieAndPerson(media.getCinevo_id(), person.getCinevo_id()).orElseGet(Credit::new);
        credit.setCredit_type(credit_type);
        credit.setCast_id(movieResponse.getCast_id());
        credit.setCharacter(movieResponse.getCharacter() != null ? movieResponse.getCharacter() : credit.getCharacter());
        credit.setCredit_id(movieResponse.getCredit_id());
        credit.setOrder(movieResponse.getOrder());
        credit.setEpisode_count(movieResponse.getEpisode_count());
        credit.setFirst_credit_air_date(movieResponse.getFirst_air_date() != null && !movieResponse.getFirst_air_date().isEmpty() ? LocalDate.parse(
                movieResponse.getFirst_air_date()) : null);
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

        credit.setMovie(media.getId() != null ? media : null);
        credit.setPerson(person.getId() != null ? person : null);

        return credit;
    }
}
