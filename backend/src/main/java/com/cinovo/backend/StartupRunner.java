package com.cinovo.backend;

import com.cinovo.backend.DB.Service.GenreService;
import com.cinovo.backend.DB.Service.MediaService;
import com.cinovo.backend.DB.Service.SpokenLanguageService;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.Common.ChangesResponse;
import com.cinovo.backend.TMDB.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner
{
    private final GenreService genreService;
    private final MediaService mediaService;
    private final SpokenLanguageService spokenLanguageService;
    private final Service service;

    public StartupRunner(GenreService genreService, MediaService mediaService, SpokenLanguageService spokenLanguageService, Service service)
    {
        this.genreService = genreService;
        this.mediaService = mediaService;
        this.spokenLanguageService = spokenLanguageService;
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception
    {
        this.genreService.findGenresByType(MediaType.MOVIE);
        this.genreService.findGenresByType(MediaType.TV);
        this.spokenLanguageService.findAllSpokenLanguages();

        this.collectChangeData(MediaType.MOVIE);
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
        while(changesResponse.getTotal_pages() >= page);
    }

    @Async
    public void processChangeAsync(Integer id, MediaType type) {
        this.mediaService.getMediaByIdAndType(id, type);
    }
}