package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Collection.TABLE_AS + ".* FROM " + Collection.TABLE_NAME + Collection.TABLE_AS + " WHERE " + Collection.TMDB_ID
                    + " = :tmdb_id")
    Optional<Collection> findByTmdbId(@Param("tmdb_id") Integer tmdb_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO collection (cinevo_id, last_update, tmdb_id, `name`, overview, poster_path, backdrop_path, original_language, original_name, adult)
            VALUES (:cinevo_id, NOW(), :tmdb_id, :name, :overview, :poster_path, :backdrop_path, :original_language, :original_name, :adult)
            ON DUPLICATE KEY UPDATE 
                last_update = NOW(),
                `name` = VALUES(`name`),
                overview = VALUES(overview),
                poster_path = VALUES(poster_path),
                backdrop_path = VALUES(backdrop_path),
                original_language = VALUES(original_language),
                original_name = VALUES(original_name),
                adult = VALUES(adult)
            """)
    void updateOrInsert(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("name") String name,
            @Param("overview") String overview, @Param("poster_path") String poster_path, @Param("backdrop_path") String backdrop_path,
            @Param("original_language") String original_language, @Param("original_name") String original_name, @Param("adult") Boolean adult);
}