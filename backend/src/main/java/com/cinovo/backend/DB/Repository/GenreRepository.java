package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Model.View.GenreView;
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
            value = "SELECT " + Genre.TABLE_AS + ".* FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.TYPE + " = :media_type")
    Optional<List<Genre>> getGenreByMediaType(@Param("media_type") String media_type);

    @Query(nativeQuery = true, value = """
                SELECT 
                        g.name AS name,
                        g.type AS type,
                        g.cinevo_id AS cinevo_id
                FROM Genre g WHERE g.type = :media_type ORDER BY g.name
            """)
    Optional<List<GenreView>> getGenreViewByMediaType(@Param("media_type") String media_type);

    @Query(nativeQuery = true,
            value = "SELECT " + Genre.TABLE_AS + ".* FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.TYPE + " = :type AND "
                    + Genre.TMDB_ID + " = :tmdb_id")
    Optional<Genre> findGenresByTmdbIdAndType(@Param("tmdb_id") final Integer tmdb_id, @Param("type") final String type);

    @Query(nativeQuery = true, value = "SELECT g.* FROM Genre g WHERE g.type = :media_type")
    Optional<List<Genre>> findByMediaType(@Param("media_type") String media_type);

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
