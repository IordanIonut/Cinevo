package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String> {
    @Query(value = "SELECT " + Genre.TABLE_AS + ".* FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.TYPE + " = :type", nativeQuery = true)
    Optional<List<Genre>> findGenresByType(@Param("type") String type);

    @Query(value = "SELECT " + Genre.TABLE_AS + ".* FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.TYPE + " = :type AND " + Genre.ID + " = :id", nativeQuery = true)
    Optional<Genre> findGenresByIdAndType(@Param("id") final Integer id, @Param("type") final String type);
}
