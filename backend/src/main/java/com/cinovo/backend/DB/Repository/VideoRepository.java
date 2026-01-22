package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Video;
import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.NonNullApi;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
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

    @Query(value = "SELECT " + Video.TABLE_AS + ".* FROM " + Video.TABLE_NAME + Video.TABLE_AS + " WHERE " + Video.TMDB_ID + " = :tmdb_id",
            nativeQuery = true)
    Optional<Video> findByTmdbId(@Param("tmdb_id") final String tmdb_id);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO video (cinevo_id, last_update, iso_639_1, iso_3166_1, name, `key` ,site, `type`, official, published_at, tmdb_id, media_id, season_id, episode_id)
            VALUES (:cinevo_id, NOW(), :iso_639_1, :iso_3166_1, :name, :key, :site, :type, :official, :published_at, :tmdb_id, :media ,:season, :episode)
            ON DUPLICATE KEY UPDATE 
                last_update = NOW(),
                iso_639_1 = VALUES(iso_639_1),
                iso_3166_1 = VALUES(iso_3166_1),     
                name = VALUES(name),
                `key` = VALUES(`key`),
                site = VALUES(site),
                `type` = VALUES(`type`),
                official = VALUES(official),
                published_at = VALUES(published_at),
                tmdb_id = VALUES(tmdb_id),
                media_id = VALUES(media_id),
                season_id = VALUES(season_id),
                episode_id = VALUES(episode_id)
            """, nativeQuery = true)
    void upsertOrInsert(@Param("cinevo_id") String cinevo_id, @Param("iso_639_1") String iso_639_1, @Param("iso_3166_1") String iso_3166_1,
            @Param("name") String name, @Param("key") String key, @Param("site") String site, @Param("type") String type,
            @Param("official") Boolean official, @Param("published_at") LocalDate published_at, @Param("tmdb_id") String tmdb_id,
            @Param("media") String media, @Param("season") String season, @Param("episode") String episode);
}
