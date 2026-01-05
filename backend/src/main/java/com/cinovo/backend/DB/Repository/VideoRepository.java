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
    @Query(nativeQuery = true,
            value = "SELECT " + Video.TABLE_AS + ".* FROM " + Video.TABLE_NAME + Video.TABLE_AS + " JOIN " + Media.TABLE_NAME + Media.TABLE_AS
                    + " ON " + Video.JOIN_MEDIA + " = " + Media.CINEVO_ID + " WHERE " + Media.TMDB_ID + " = :media_tmdb_id")
    Optional<List<Video>> findByMediaTmdbId(@Param("media_tmdb_id") final Integer media_tmdb_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Video.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + Video.TABLE_NAME + Video.TABLE_AS
                    + " ON " + Video.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Media.Season.CINEVO_ID + " = " + Video.SEASON_ID + " WHERE " + Media.TMDB_ID + " = :media_tmdb_id AND "
                    + Media.Season.SEASON_NUMBER + " = :season_number")
    Optional<List<Video>> findByMediaTmdbIdAndSeasonNumber(@Param("media_tmdb_id") final Integer media_tmdb_id,
            @Param("season_number") final Integer season_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Video.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + Video.TABLE_NAME + Video.TABLE_AS
                    + " ON " + Video.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Media.Season.CINEVO_ID + " = " + Video.SEASON_ID + " JOIN " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " ON " + Media.Season.CINEVO_ID + " = " + Media.Season.Episode.SEASON_ID + " WHERE " + Media.TMDB_ID + " = :media_tmdb_id AND "
                    + Media.Season.SEASON_NUMBER + " = :season_number AND " + Media.Season.Episode.EPISODE_NUMBER + " = :episode_number")
    Optional<List<Video>> findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(@Param("media_tmdb_id") final Integer media_tmdb_id,
            @Param("season_number") final Integer season_number, @Param("episode_number") final Integer episode_number);

    @Query(value = "SELECT " + Video.TABLE_AS + ".* FROM " + Video.TABLE_NAME + Video.TABLE_AS + " WHERE " + Video.TMDB_ID + " = :tmdb_id", nativeQuery = true)
    Optional<Video> findByTmdbId(@Param("tmdb_id") final String tmdb_id);
}
