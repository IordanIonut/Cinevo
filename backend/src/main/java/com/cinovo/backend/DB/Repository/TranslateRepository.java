package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Translate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
