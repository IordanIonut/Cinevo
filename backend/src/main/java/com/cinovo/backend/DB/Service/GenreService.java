package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Repository.GenreRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.DTO.GenresResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@JBossLog
public class GenreService implements TMDBLogically<Type, List<Genre>> {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;

    public List<Genre> findGenresByType(final Type type) throws Exception {
        Optional<List<Genre>> genres = this.genreRepository.findGenresByType(type.name());
        if (genres.isEmpty() || genres.get().isEmpty()) {
            return  onConvertTMDB(type);
        }
//TODO: create a schedule job what will make update of data by a time .... to use the same method when start a server
//        if(genres.get().get(0).getLastUpdate().equals(LocalDate.now())){
//            this.deleteAllGenresByType(genres.get());
//            return  onConvertTMDB(this.controller.getGenres(type), type);
//        }
        return genres.get();
    }

    @Override
    public List<Genre> onConvertTMDB(Type type) throws Exception {
        List<Genre> convertedGenres = new ArrayList<>();
        for (GenresResponse.Genre g : this.service.getGenres(type).getGenres()) {
            Genre genre = new Genre();
            genre.setType(type);
            genre.setId(g.getId());
            genre.setName(g.getName());
            genre.setLastUpdate(LocalDate.now());
            convertedGenres.add(genre);
        }

        this.genreRepository.saveAll(convertedGenres);
        return convertedGenres;    }
}
