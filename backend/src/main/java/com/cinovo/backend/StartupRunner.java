package com.cinovo.backend;

import com.cinovo.backend.DB.Service.GenreService;
import com.cinovo.backend.Enum.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    @Autowired
    private GenreService genreService;

    @Override
    public void run(String... args) throws Exception {
        /// GENRES
        this.genreService.findGenresByType(MediaType.MOVIE);
        this.genreService.findGenresByType(MediaType.TV);
    }
}