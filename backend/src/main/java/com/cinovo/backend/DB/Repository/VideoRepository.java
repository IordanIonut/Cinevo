package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Video;
import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.NonNullApi;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, String>
{
    @Query(value = "SELECT " + Video.TABLE_AS + ".* FROM " + Video.TABLE_NAME + Video.TABLE_AS + " JOIN " + Media.TABLE_NAME + Media.TABLE_AS + " ON "
            + Video.JOIN_MEDIA + " = " + Media.CINEVO_ID + " WHERE " + Media.ID + " = :movie_id", nativeQuery = true)
    Optional<List<Video>> findVideosByMovieIdAndMediaType(@Param("movie_id") final Integer movie_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Video.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + Video.TABLE_NAME + Video.TABLE_AS
                    + " ON " + Video.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Media.Season.CINEVO_ID + " = " + Video.SEASON_ID + " WHERE " + Media.ID + " = :series_id AND " + Media.Season.SEASON_NUMBER
                    + " = :season_number")
    Optional<List<Video>> findVideosBySeriesIdAndMediaType(@Param("series_id") final Integer series_id,
            @Param("season_number") final Integer season_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Video.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + Video.TABLE_NAME + Video.TABLE_AS
                    + " ON " + Video.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Media.Season.CINEVO_ID + " = " + Video.SEASON_ID + " JOIN " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " ON " + Media.Season.CINEVO_ID + " = " + Media.Season.Episode.SEASON_ID + " WHERE " + Media.ID + " = :series_id AND "
                    + Media.Season.SEASON_NUMBER + " = :season_number AND " + Media.Season.Episode.EPISODE_NUMBER + " = :episode")
    Optional<List<Video>> findVideoBySeriesIdAndSeasonNumberAndEpisodeAndMediaType(@Param("series_id") final Integer series_id,
            @Param("season_number") final Integer season_number, @Param("episode") final Integer episode);

    @Query(value = "SELECT " + Video.TABLE_AS + ".* FROM " + Video.TABLE_NAME + Video.TABLE_AS + " WHERE " + Video.ID + " = :id", nativeQuery = true)
    Optional<Video> findById(@Param("id") final String id);
}
