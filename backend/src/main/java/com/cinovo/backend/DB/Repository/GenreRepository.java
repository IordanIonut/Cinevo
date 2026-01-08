package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Genre;
import jdk.jfr.TransitionTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Genre.TABLE_AS + ".* FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.TYPE + " = :type")
    Optional<List<Genre>> getGenreByMediaType(@Param("type") String type);

    @Query(nativeQuery = true,
            value = "SELECT " + Genre.TABLE_AS + ".* FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.TYPE + " = :type AND "
                    + Genre.TMDB_ID + " = :tmdb_id")
    Optional<Genre> findGenresByTmdbIdAndType(@Param("tmdb_id") final Integer tmdb_id, @Param("type") final String type);

    @Query(nativeQuery = true, value = "SELECT g.* FROM Genre g WHERE g.type = :type")
    Optional<List<Genre>> findByMediaType(@Param("type") String type);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO genre(cinevo_id, last_update, tmdb_id, `name`, `type`)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :name, :type)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    `name` = VALUES(`name`),
                    `type` = VALUES(`type`)
            """)
    void updateOrInsert(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("name") String name,
            @Param("type") String type);
}
