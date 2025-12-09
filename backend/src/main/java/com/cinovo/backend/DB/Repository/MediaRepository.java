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
    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.TABLE_AS + ".* FROM " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " WHERE " + Media.Season.ID
                    + " = :id")
    Optional<Media.Season> getSeasonById(@Param("id") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.Episode.TABLE_AS + ".* FROM " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " WHERE " + Media.Season.Episode.ID + " = :id")
    Optional<Media.Season.Episode> getEpisodeById(@Param("id") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.TABLE_AS + ".* FROM " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " JOIN " + Media.TABLE_NAME
                    + Media.TABLE_AS + " ON " + Media.Season.JOIN_MEDIA + " = " + Media.CINEVO_ID + " WHERE " + Media.ID + " = :id AND "
                    + Media.Season.SEASON_NUMBER + " = :season_number")
    Optional<Media.Season> getSeasonByMediaIdAndSeasonNumber(@Param("id") final Integer id, @Param("season_number") final Integer season_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.Episode.TABLE_AS + ".* FROM " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " JOIN "
                    + Media.TABLE_NAME + Media.TABLE_AS + " ON " + Media.Season.JOIN_MEDIA + " = " + Media.CINEVO_ID + " JOIN "
                    + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS + " ON " + Media.Season.CINEVO_ID + " = "
                    + Media.Season.Episode.SEASON_ID + " WHERE " + Media.ID + " = :id AND " + Media.Season.SEASON_NUMBER + " = :season_number AND "
                    + Media.Season.Episode.EPISODE_NUMBER + " = :episode_number")
    Optional<Media.Season.Episode> getEpisodeByMediaIdAndSeasonNumberAndEpisode(@Param("id") final Integer id,
            @Param("season_number") final Integer season_number, @Param("episode_number") final Integer episode_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.EpisodeToAir.TABLE_AS + ".* FROM " + Media.EpisodeToAir.TABLE_NAME + Media.EpisodeToAir.TABLE_AS + " WHERE "
                    + Media.EpisodeToAir.ID + " = :id")
    Optional<Media.EpisodeToAir> getEpisodeToAirById(@Param("id") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.TABLE_AS + ".*" + " FROM " + Media.TABLE_NAME + Media.TABLE_AS + " WHERE " + Media.ID + " = :id AND "
                    + Media.TYPE + " = :type")
    Optional<Media> getMediaByIdAndType(@Param("id") final Integer id, @Param("type") final String type);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.Episode.TABLE_AS + ".* FROM " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " WHERE " + Media.Season.Episode.ID + " = :episode_id")
    Optional<Media.Season.Episode> findEpisodeById(@Param("episode_id") final Integer episode_id);
}
