package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, String>
{
    @Query(nativeQuery = true, value = "SELECT " + Media.Season.TABLE_AS + ".* FROM " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " WHERE "
            + Media.Season.TMDB_ID + " = :season_tmdb_id")
    Optional<Media.Season> getSeasonBySeasonTmdbId(@Param("season_tmdb_id") Integer season_tmdb_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.Episode.TABLE_AS + ".* FROM " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " WHERE " + Media.Season.Episode.TMDB_ID + " = :episode_tmdb_id")
    Optional<Media.Season.Episode> getEpisodeByEpisodeTmdbId(@Param("episode_tmdb_id") Integer episode_tmdb_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.TABLE_AS + ".* FROM " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " JOIN " + Media.TABLE_NAME
                    + Media.TABLE_AS + " ON " + Media.Season.JOIN_MEDIA + " = " + Media.CINEVO_ID + " WHERE " + Media.TMDB_ID
                    + " = :media_tmdb_id AND " + Media.Season.SEASON_NUMBER + " = :season_number")
    Optional<Media.Season> getSeasonByMediaTmdbIdAndSeasonNumber(@Param("media_tmdb_id") final Integer media_tmdb_id,
            @Param("season_number") final Integer season_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.Episode.TABLE_AS + ".* FROM " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " JOIN "
                    + Media.TABLE_NAME + Media.TABLE_AS + " ON " + Media.Season.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN "
                    + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS + " ON " + Media.Season.CINEVO_ID + " = "
                    + Media.Season.Episode.SEASON_ID + " WHERE " + Media.TMDB_ID + " = :media_tmdb_id AND " + Media.Season.SEASON_NUMBER
                    + " = :season_number AND " + Media.Season.Episode.EPISODE_NUMBER + " = :episode_number")
    Optional<Media.Season.Episode> getEpisodeByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(@Param("media_tmdb_id") final Integer media_tmdb_id,
            @Param("season_number") final Integer season_number, @Param("episode_number") final Integer episode_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.EpisodeToAir.TABLE_AS + ".* FROM " + Media.EpisodeToAir.TABLE_NAME + Media.EpisodeToAir.TABLE_AS + " WHERE "
                    + Media.EpisodeToAir.TMDB_ID + " = :episode_to_air_tmdb_id")
    Optional<Media.EpisodeToAir> getEpisodeToAirByTmdbId(@Param("episode_to_air_tmdb_id") Integer episode_to_air_tmdb_id);

    @Query(nativeQuery = true, value = "SELECT " + Media.TABLE_AS + ".*" + " FROM " + Media.TABLE_NAME + Media.TABLE_AS + " WHERE " + Media.TMDB_ID
            + " = :media_tmdb_id AND " + Media.TYPE + " = :type")
    Optional<Media> getMediaByTmdbIdAndMediaType(@Param("media_tmdb_id") final Integer media_tmdb_id, @Param("type") final String type);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.Episode.TABLE_AS + ".* FROM " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " WHERE " + Media.Season.Episode.TMDB_ID + " = :episode_tmdb_id")
    Optional<Media.Season.Episode> findEpisodeByTmdbId(@Param("episode_tmdb_id") final Integer episode_tmdb_id);
}
