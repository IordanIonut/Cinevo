package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.WatchProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchProviderRepository extends JpaRepository<WatchProvider, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + WatchProvider.TABLE_AS + ".* FROM " + WatchProvider.TABLE_NAME + WatchProvider.TABLE_AS + " JOIN " + Media.TABLE_NAME
                    + Media.TABLE_AS + " ON " + WatchProvider.JOIN_MEDIA + " = " + Media.CINEVO_ID + " WHERE " + Media.TMDB_ID + " = :media_tmdb_id")
    Optional<List<WatchProvider>> findByMediaTmdbId(@Param("media_tmdb_id") final Integer media_tmdb_id);

    @Query(nativeQuery = true,
            value = "SELECT " + WatchProvider.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + WatchProvider.TABLE_NAME
                    + WatchProvider.TABLE_AS + " ON " + WatchProvider.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME
                    + Media.Season.TABLE_AS + " ON " + Media.Season.CINEVO_ID + " = " + WatchProvider.SEASON_ID + " WHERE " + Media.TMDB_ID
                    + " = :media_tmdb_id AND " + Media.Season.SEASON_NUMBER + " = :season_number ")
    Optional<List<WatchProvider>> findByMediaTmdbIdAndSeasonNumber(@Param("media_tmdb_id") final Integer media_tmdb_id,
            @Param("season_number") final Integer season_number);

    @Query(nativeQuery = true,
            value = "SELECT " + WatchProvider.TABLE_AS + ".* FROM " + WatchProvider.TABLE_NAME + WatchProvider.TABLE_AS + " LEFT JOIN "
                    + Media.TABLE_NAME + Media.TABLE_AS + " ON " + WatchProvider.JOIN_MEDIA + " = " + Media.CINEVO_ID + " LEFT JOIN "
                    + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON " + Media.CINEVO_ID + " = " + Media.Season.CINEVO_ID
                    + " WHERE (:media_cinevo_id IS NULL OR " + Media.CINEVO_ID + " = :media_cinevo_id) AND (:season_cinevo_id IS NULL OR "
                    + WatchProvider.JOIN_MEDIA_SEASON + " = :season_cinevo_id) AND " + WatchProvider.TYPE + " = :type AND "
                    + WatchProvider.PROVIDER_TYPE + " = :provider_type AND " + WatchProvider.LOCATION + " = :location AND "
                    + WatchProvider.PROVIDER_ID + " = :provider_id")
    Optional<WatchProvider> findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(
            @Param("media_cinevo_id") final String media_cinevo_id, @Param("season_cinevo_id") final String season_cinevo_id,
            @Param("type") final String type, @Param("provider_type") final String provider_type, @Param("location") final String location,
            @Param("provider_id") final Integer provider_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                        INSERT INTO watch_provider(cinevo_id, last_update, `type`, provider_type, location, logo_path, provider_id, provider_name, display_priority, media_id, season_id)
                        VALUES(:cinevo_id, NOW(), :type, :provider_type, :location, :logo_path, :provider_id, :provider_name, :display_priority, :media_id, :season_id)
                        ON DUPLICATE KEY UPDATE 
                            last_update = NOW(),
                            `type` = VALUES(`type`),
                            provider_type = VALUES(provider_type),
                            location = VALUES(location),
                            logo_path = VALUES(logo_path),
                            provider_id = VALUES(provider_id),
                            provider_name = VALUES(provider_name),
                            display_priority = VALUES(display_priority),
                            media_id = VALUES(media_id),
                            season_id = VALUES(season_id)
            """)
    void updateOrInsert(@Param("cinevo_id") String cinevo_id, @Param("type") String type, @Param("provider_type") String provider_type,
            @Param("location") String location, @Param("logo_path") String logo_path, @Param("provider_id") Integer provider_id,
            @Param("provider_name") String provider_name, @Param("display_priority") Integer display_priority, @Param("media_id") String media_id,
            @Param("season_id") String season_id);
}
