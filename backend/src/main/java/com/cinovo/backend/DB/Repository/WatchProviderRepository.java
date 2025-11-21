package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.WatchProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchProviderRepository extends JpaRepository<WatchProvider, String>
{
    @Query(value = "SELECT " + WatchProvider.TABLE_AS + ".* FROM " + WatchProvider.TABLE_NAME + WatchProvider.TABLE_AS + " JOIN " + Media.TABLE_NAME
            + Media.TABLE_AS + " ON " + WatchProvider.JOIN_MEDIA + " = " + Media.CINEVO_ID + " WHERE " + Media.ID + " = :media_id",
            nativeQuery = true)
    Optional<List<WatchProvider>> findWatchProviderByMovieId(@Param("media_id") final Integer media_id);

    @Query(nativeQuery = true,
            value = "SELECT " + WatchProvider.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + WatchProvider.TABLE_NAME
                    + WatchProvider.TABLE_AS + " ON " + WatchProvider.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME
                    + Media.Season.TABLE_AS + " ON " + Media.Season.CINEVO_ID + " = " + WatchProvider.SEASON_ID + " WHERE " + Media.ID
                    + " = :season_id AND " + Media.Season.SEASON_NUMBER + " = :season_number ")
    Optional<List<WatchProvider>> findWatchProviderBySeasonIdAndType(@Param("season_id") final Integer season_id,
            @Param("season_number") final Integer season_number);

    @Query(nativeQuery = true,
            value = "SELECT " + WatchProvider.TABLE_AS + ".* FROM " + WatchProvider.TABLE_NAME + WatchProvider.TABLE_AS + " JOIN " + Media.TABLE_NAME
                    + Media.TABLE_AS + " ON " + WatchProvider.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME
                    + Media.Season.TABLE_AS + " ON " + Media.CINEVO_ID + " = " + Media.Season.CINEVO_ID + " WHERE (:media_id IS NULL OR "
                    + Media.CINEVO_ID + " = :media_id) AND (:season_id IS NULL OR " + Media.Season.CINEVO_ID + " = :season_id) AND "
                    + WatchProvider.TYPE + " = :type AND " + WatchProvider.PROVIDER_TYPE + " = :provider_type AND " + WatchProvider.LOCATION
                    + " = :location AND " + WatchProvider.PROVIDER_ID + " = :provider_id")
    Optional<WatchProvider> findWatchProviderById(@Param("media_id") final String media_id, @Param("season_id") final String season_id,
            @Param("type") final String type, @Param("provider_type") final String provider_type, @Param("location") final String location,
            @Param("provider_id") final Integer provider_id);
}
