package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Movie;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.CreditRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.CreditType;
import com.cinovo.backend.Enum.Gender;
import com.cinovo.backend.TMDB.Response.CreditResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@JBossLog
public class CreditService implements TMDBLogically<Integer, List<Credit>>
{
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private MovieService movieService;
    @Autowired
    private PersonService personService;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;

    public List<Credit> findCreditByMovieId(final Integer movie_id) throws Exception
    {
        Optional<List<Credit>> credits = this.creditRepository.findCreditByMovieId(movie_id);
        if(credits.isEmpty() || credits.get().isEmpty())
        {
            return this.onConvertTMDB(movie_id);
        }
        return credits.get();
    }

    @Override
    public List<Credit> onConvertTMDB(Integer movie_id) throws Exception
    {
        CreditResponse creditResponse = this.service.getMovieCredit(movie_id, "en-US");
        List<Credit> credits = new ArrayList<>();
        Movie movie = movieService.getMovieById(creditResponse.getId());
        for(CreditResponse.Cast cast : creditResponse.getCast())
        {
            Person person = this.personService.findPersonById(cast.getId());
            person.setAdult(cast.getAdult());
            person.setGender(cast.getGender() == 0 ? Gender.F : Gender.M);
            person.setKnow_for_department(cast.getKnown_for_department());
            person.setOriginal_name(cast.getOriginal_name());
            person.setName(cast.getName());
            person.setProfile_file(cast.getProfile_path());
            person.setPopularity(Double.valueOf(cast.getPopularity()));
            person.setId(cast.getId());

            Credit credit = this.creditRepository.findCreditByCreditId(cast.getCredit_id()).orElseGet(Credit::new);
            credit.setCredit_type(CreditType.CAST);
            credit.setCast_id(cast.getCast_id());
            credit.setCharacter(cast.getCharacter());
            credit.setCredit_id(cast.getCredit_id());
            credit.setOrder(cast.getOrder());
            credit.setMovie(movie);
            credit.setPerson(person);

            credits.add(credit);
        }

        for(CreditResponse.Crew crew:  creditResponse.getCrew()){
            Person person = this.personService.findPersonById(crew.getId());
            person.setAdult(crew.getAdult());
            person.setGender(crew.getGender() == 0 ? Gender.F : Gender.M);
            person.setKnow_for_department(crew.getKnown_for_department());
            person.setOriginal_name(crew.getOriginal_name());
            person.setName(crew.getName());
            person.setProfile_file(crew.getProfile_path());
            person.setPopularity(Double.valueOf(crew.getPopularity()));
            person.setId(crew.getId());

            Credit credit = this.creditRepository.findCreditByCreditId(crew.getCredit_id()).orElseGet(Credit::new);
            credit.setCredit_type(CreditType.CREW);
            credit.setCredit_id(crew.getCredit_id());
            credit.setJob(crew.getJob());
            credit.setMovie(movie);
            credit.setPerson(person);

            credits.add(credit);
        }

        creditRepository.saveAll(credits);
        return credits;
    }
}
