package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Translate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslateRepository extends JpaRepository<Translate, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Translate.TABLE_AS + ".* FROM " + Translate.TABLE_NAME + Translate.TABLE_AS + " WHERE " + Translate.TMDB_ID
                    + " = :tmdb_id")
    Optional<List<Translate>> findByTmdbId(@Param("tmdb_id") final Integer tmdb_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Translate.TABLE_AS + ".* FROM " + Translate.TABLE_NAME + Translate.TABLE_AS + " WHERE " + Translate.TMDB_ID
                    + " = :tmdb_id AND " + Translate.ISO_UPPER + " = :iso")
    Optional<Translate> findByTmdbIdAndIso(@Param("tmdb_id") final Integer tmdb_id, @Param("iso") final String iso);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO translate(cinevo_id, last_update, tmdb_id, iso_upper, iso_lower, `name`, english_name, title, overview, home_page)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :iso_upper, :iso_lower, :name, :english_name, :title, :overview, :home_page)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    iso_upper = VALUES(iso_upper),
                    iso_lower = VALUES(iso_lower),
                    `name` = VALUES(`name`),
                    english_name = VALUES(english_name),
                    title = VALUES(title),
                    overview = VALUES(overview),
                    home_page = VALUES(home_page)
            """)
    void updateOrInsert(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("iso_upper") String iso_upper,
            @Param("iso_lower") String iso_lower, @Param("name") String name, @Param("english_name") String english_name,
            @Param("title") String title, @Param("overview") String overview, @Param("home_page") String home_page);
}
