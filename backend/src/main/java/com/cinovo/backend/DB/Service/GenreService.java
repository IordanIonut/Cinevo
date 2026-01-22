package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Repository.GenreRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.Common.GenresResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@JBossLog
public class GenreService implements TMDBLogically<MediaType, List<Genre>>
{
    private final GenreRepository genreRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public GenreService(GenreRepository genreRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.genreRepository = genreRepository;
        this.service = service;
    }

    public List<Genre> getGenreByMediaType(final MediaType type) throws Exception
    {
        Optional<List<Genre>> genres = this.genreRepository.getGenreByMediaType(type.name());
        if(genres.isEmpty() || genres.get().isEmpty())
        {
            return onConvertTMDB(type);
        }
        //TODO: create a schedule job what will make update of data by a time .... to use the same method when start a server
        //        if(genres.get().get(0).getLastUpdate().equals(LocalDate.now())){
        //            this.deleteAllGenresByType(genres.get());
        //            return  onConvertTMDB(this.controller.getGenres(type), type);
        //        }
        return genres.get();
    }

    public List<Genre> parsLongToObjects(final List<Integer> genre_ids, final MediaType type)
    {
        List<Genre> genres = new ArrayList<>();
        for(Integer genre : genre_ids)
        {
            genres.add(this.genreRepository.findGenresByTmdbIdAndType(genre, type.name()).get());
        }
        return genres;
    }

    public List<Genre> parsGenreToObjects(final List<GenresResponse.Genre> genres, final MediaType type)
    {
        List<Genre> list = new ArrayList<>();
        for(GenresResponse.Genre genre : genres)
        {
            Optional<Genre> gen = this.genreRepository.findGenresByTmdbIdAndType(genre.getId(), type.name());
            if(gen.isEmpty()){
                this.genreRepository.updateOrInsert(UUID.randomUUID().toString(),genre.getId(), genre.getName(), type.name());
            }
            list.add(this.genreRepository.findGenresByTmdbIdAndType(genre.getId(), type.name()).get());
        }
        return list;
    }

    @Override
    @Transactional
    public List<Genre> onConvertTMDB(MediaType type) throws Exception
    {
        for(GenresResponse.Genre g : this.service.getGenres(type).getGenres())
        {
            this.genreRepository.updateOrInsert(Shared.generateCinevoId(this.genreRepository.findGenresByTmdbIdAndType(g.getId(), type.name())),
                    g.getId(), g.getName(), type.name());
        }

        return this.genreRepository.findByMediaType(type.name()).get();
    }
}
