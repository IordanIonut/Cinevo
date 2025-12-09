package com.cinovo.backend;

import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Service.*;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.Common.ChangesResponse;
import com.cinovo.backend.TMDB.Service;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final Service service;

    public StartupRunner(GenreService genreService, MediaService mediaService, PersonService personService,
            SpokenLanguageService spokenLanguageService, ProductionCountryService productionCountryService, Service service)
    {
        this.genreService = genreService;
        this.mediaService = mediaService;
        this.personService = personService;
        this.spokenLanguageService = spokenLanguageService;
        this.productionCountryService = productionCountryService;
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception
    {
        this.genreService.findGenresByType(MediaType.MOVIE);
        this.genreService.findGenresByType(MediaType.TV);
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
                Person person = this.personService.findPersonById(id);
                if(person != null)
                {
                    this.personService.saveOrUpdate(person);
                }
            }
            else
            {
                this.mediaService.getMediaByIdAndType(id, type);
            }
        }
        catch(Exception e)
        {
            log.warn("Skipping media id=" + id + " type=" + type + " due to error: " + e.getMessage());
        }
    }
}