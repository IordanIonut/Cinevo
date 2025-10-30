package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>
{
    @Query(nativeQuery = true, value = "SELECT " + Movie.TABLE_AS+".*" + " FROM " + Movie.TABLE_NAME + Movie.TABLE_AS + " WHERE " + Movie.ID + " = :id")
    Optional<Movie> getMovieById(@Param("id") final Integer id);
}
