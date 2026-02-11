package com.cinovo.backend.Schedule;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Service.*;
import com.cinovo.backend.TMDB.Response.Common.ChangesResponse;
import com.cinovo.backend.TMDB.Service;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@JBossLog
public class StartupRunner implements CommandLineRunner
{
    private final GenreService genreService;
    private final MediaService mediaService;
    private final PersonService personService;
    private final SpokenLanguageService spokenLanguageService;
    private final ProductionCountryService productionCountryService;
    private final CountryService countryService;
    private final Service service;
    private final Job job;

    public StartupRunner(GenreService genreService, MediaService mediaService, PersonService personService,
            SpokenLanguageService spokenLanguageService, ProductionCountryService productionCountryService, CountryService countryService,
            Service service, Job job)
    {
        this.genreService = genreService;
        this.mediaService = mediaService;
        this.personService = personService;
        this.spokenLanguageService = spokenLanguageService;
        this.productionCountryService = productionCountryService;
        this.countryService = countryService;
        this.service = service;
        this.job = job;
    }

    @Override
    public void run(String... args) throws Exception
    {
        this.genreService.getGenreByMediaType(MediaType.MOVIE);
        this.genreService.getGenreByMediaType(MediaType.TV);
        this.countryService.findCountryByMediaType(MediaType.MOVIE);
        this.countryService.findCountryByMediaType(MediaType.TV);
        this.spokenLanguageService.findAllSpokenLanguages();
        this.productionCountryService.findAllProductionCountry();

        this.collectChangeData(MediaType.MOVIE);
        this.collectChangeData(MediaType.TV);
        this.collectChangeData(MediaType.PERSON);
    }

    public void collectChangeData(final MediaType type) throws Exception
    {
        int page = 0;
        ChangesResponse changesResponse = new ChangesResponse();
        do
        {
            page++;
            changesResponse = this.service.getChangeResponse(MediaType.MOVIE, null, page, null);
            for(ChangesResponse.Change change : changesResponse.getResults())
            {
                this.processChangeAsync(change.getId(), type);
            }
        }
                while(page <= changesResponse.getTotal_pages());
//        while(page < 1);
    }

    @Async("customExecutorStartupRunner")
    public void processChangeAsync(Integer id, MediaType type) throws Exception
    {
        try
        {
            if(type.equals(MediaType.PERSON))
            {
                Person person = this.personService.findByTmdbId(id);
//                if(person != null)
//                {
//                    this.personService.saveOrUpdate(person);
//                }
            }
            else
            {
                this.mediaService.getMediaByTmdbIdAndMediaType(id, type);
            }
        }
        catch(Exception e)
        {
            log.warn("Skipping media id=" + id + " type=" + type + " due to error: " + e.getMessage());
        }
    }
}