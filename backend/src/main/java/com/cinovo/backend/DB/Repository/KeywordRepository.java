package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Keyword;
import com.cinovo.backend.DB.Model.View.KeywordView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Keyword.TABLE_AS + ".* FROM " + Keyword.TABLE_NAME + Keyword.TABLE_AS + " WHERE " + Keyword.TMDB_ID + " = :tmdb_id")
    Optional<Keyword> findByTmdbId(@Param("tmdb_id") final Integer tmdb_id);

    @Query(nativeQuery = true, value = """
                SELECT
                    k.cinevo_id AS cinevo_id,
                    k.name AS name
                FROM Keyword k 
                WHERE k.name LIKE :name
            """)
    Optional<List<KeywordView>> findKeywordViewByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO keyword(cinevo_id, last_update, tmdb_id, name)
            VALUES (:cinevo_id, NOW(), :tmdb_id, :name)
            ON DUPLICATE KEY UPDATE
                last_update = NOW(),
                tmdb_id = VALUES(tmdb_id),
                name = VALUES(name)
            """)
    void updateOrInsert(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("name") String name);
}
