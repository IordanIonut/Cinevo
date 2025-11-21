package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Repository.GenreRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.Common.GenresResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Genre> findGenresByType(final MediaType type) throws Exception
    {
        Optional<List<Genre>> genres = this.genreRepository.findGenresByType(type.name());
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
            genres.add(this.genreRepository.findGenresByIdAndType(genre, type.name()).get());
        }
        return genres;
    }

    public List<Genre> parsGenreToObjects(final List<GenresResponse.Genre> genres, final MediaType type)
    {
        List<Genre> list = new ArrayList<>();
        for(GenresResponse.Genre genre : genres)
        {
            list.add(this.genreRepository.findGenresByIdAndType(genre.getId(), type.name()).get());
        }
        return list;
    }

    @Override
    public List<Genre> onConvertTMDB(MediaType type) throws Exception
    {
        List<Genre> convertedGenres = new ArrayList<>();
        for(GenresResponse.Genre g : this.service.getGenres(type).getGenres())
        {
            Genre genre = this.genreRepository.findGenresByIdAndType(g.getId(), type.name()).orElse(new Genre());
            genre.setType(type);
            genre.setId(g.getId());
            genre.setName(g.getName());
            genre.setLastUpdate(LocalDate.now());
            convertedGenres.add(genre);
        }

        this.genreRepository.saveAll(convertedGenres);
        return convertedGenres;
    }
}
