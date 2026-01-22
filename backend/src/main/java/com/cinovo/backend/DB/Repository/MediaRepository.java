package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.View.MediaView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, String>
{
    String sqlMediaView = """
              SELECT
                m.tmdb_id AS tmdb_id,
                CONCAT(:url ,m.poster_path) AS poster_path,
                m.type AS type,
                m.cinevo_id AS cinevo_id,
                m.title AS title,
                m.release_date AS release_date,
                m.runtime AS runtime,
                m.vote_average AS vote_average,
                COUNT(s.cinevo_id) AS seasons_numbe,
                m.first_air_date as first_air_date,
                v.video_key AS `key`,
                v.site AS site
            FROM Media m
                LEFT JOIN Season s
                ON m.cinevo_id = s.media_id
                        LEFT JOIN (
                SELECT media_id, `key` AS video_key,  site
                FROM (
                    SELECT
                        v1.media_id,
                        v1.`key`,
                        v1.site ,
                        v1.published_at,
                        ROW_NUMBER() OVER (
                            PARTITION BY v1.media_id
                            ORDER BY v1.published_at DESC, v1.tmdb_id DESC
                        ) AS rn
                    FROM Video v1
                    WHERE v1.official = 1
                      AND v1.`type` = 'TRAILER'
                ) AS ranked
                WHERE rn = 1
                        ) AS v
                ON m.cinevo_id = v.media_id""";

    @Query(nativeQuery = true, value = "SELECT " + Media.Season.TABLE_AS + ".* FROM " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " WHERE "
            + Media.Season.TMDB_ID + " = :season_tmdb_id")
    Optional<Media.Season> getSeasonBySeasonTmdbId(@Param("season_tmdb_id") Integer season_tmdb_id);

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

    @Query(nativeQuery = true, value = "SELECT " + Media.TABLE_AS + ".*" + " FROM " + Media.TABLE_NAME + Media.TABLE_AS + " WHERE " + Media.TMDB_ID
            + " = :media_tmdb_id AND " + Media.TYPE + " = :type")
    Optional<Media> getMediaByTmdbIdAndMediaType(@Param("media_tmdb_id") final Integer media_tmdb_id, @Param("type") final String type);

    @Query(nativeQuery = true,
            value = "SELECT " + Media.Season.Episode.TABLE_AS + ".* FROM " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " WHERE " + Media.Season.Episode.TMDB_ID + " = :episode_tmdb_id")
    Optional<Media.Season.Episode> findEpisodeByTmdbId(@Param("episode_tmdb_id") final Integer episode_tmdb_id);

    @Query(nativeQuery = true, value = """
                SELECT m.* FROM MEDIA m WHERE m.TMDB_ID IN (:ids) AND m.TYPE = :media_type ORDER BY m.VOTE_AVERAGE DESC, m.VOTE_COUNT DESC
            """)
    Optional<List<Media>> findByMediaTypeAndTmdbIds(@Param("ids") List<Integer> ids, @Param("media_type") final String media_type);

    @Query(nativeQuery = true, value = sqlMediaView + " WHERE m.type = :media_type AND m.tmdb_id IN (:ids) GROUP BY m.cinevo_id, v.video_key, v.site")
    Optional<List<MediaView>> getMediaUsingIds(@Param("url") String url, @Param("media_type") String media_type, @Param("ids") List<Integer> ids);

    @Query(nativeQuery = true, value = sqlMediaView + " GROUP BY m.cinevo_id, v.video_key, v.site ORDER BY RAND() LIMIT 20;")
    Optional<List<MediaView>> getRandomByMediaType(@Param("url") String url, @Param("media_type") String media_type);

    @Query(nativeQuery = true, value = "SELECT eta.* FROM EPISODE_TO_AIR eta WHERE eta.TMDB_ID = :tmdb_id")
    Optional<Media.EpisodeToAir> findEpisodeToAirByTmdbId(@Param("tmdb_id") Integer tmdb_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO media(cinevo_id, last_update, tmdb_id, `type`, adult, backdrop_path, budget, homepage,
                original_language, original_title, overview, popularity, poster_path, release_date, first_air_date, revenue, 
                runtime, status, tagline, title, video, vote_average, vote_count)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :type, :adult, :backdrop_path, :budget, :homepage, :original_language,
                :original_title, :overview, :popularity, :poster_path, :release_date, :first_air_date, :revenue, :runtime, 
                :status, :tagline, :title, :video, :vote_average, :vote_count)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    tmdb_id = VALUES(tmdb_id),
                    `type` = VALUES(`type`),
                    adult = VALUES(adult),
                    backdrop_path = VALUES(backdrop_path),
                    budget = VALUES(budget),
                    homepage = VALUES(homepage),
                    original_language = VALUES(original_language),
                    original_title = VALUES(original_title),
                    overview = VALUES(overview),
                    popularity = VALUES(popularity),
                    poster_path = VALUES(poster_path),
                    release_date = VALUES(release_date),
                    first_air_date = VALUES(first_air_date),
                    revenue = VALUES(revenue),
                    runtime = VALUES(runtime),
                    status = VALUES(status),
                    tagline = VALUES(tagline),
                    title = VALUES(title),
                    video = VALUES(video),
                    vote_average = VALUES(vote_average),
                    vote_count = VALUES(vote_count)
            """)
    void updateOrInsertMedia(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("type") String type,
            @Param("adult") Boolean adult, @Param("backdrop_path") String backdrop_path, @Param("budget") Integer budget,
            @Param("homepage") String homepage, @Param("original_language") String original_language, @Param("original_title") String original_title,
            @Param("overview") String overview, @Param("popularity") Double popularity, @Param("poster_path") String poster_path,
            @Param("release_date") LocalDate release_date, @Param("first_air_date") LocalDate first_air_date, @Param("revenue") Long revenue,
            @Param("runtime") Integer runtime, @Param("status") String status, @Param("tagline") String tagline, @Param("title") String title,
            @Param("video") Boolean video, @Param("vote_average") Double vote_average, @Param("vote_count") Integer vote_count);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO media_language(media_id, language)
                VALUES(:media_id, :language)
            """)
    void updateOrInsertLanguage(@Param("media_id") String media_id, @Param("language") String language);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO media_episode_run_time(media_id, episode_run_time)
                VALUES(:media_id, :episode_run_time)
            """)
    void updateOrInsertEpisodeRunTime(@Param("media_id") String media_id, @Param("episode_run_time") Integer episode_run_time);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO media_keyword(media_id, keyword_id)
                VALUES(:media_id, :keyword_id)
            """)
    void updateOrInsertMediaKeyword(@Param("media_id") String media_id, @Param("keyword_id") String keyword_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT IGNORE INTO media_origin_country(media_id, country_id)
            VALUES(:media_id, :country_id)
            """)
    void updateOrInsertMediaOriginCountry(@Param("media_id") String media_id, @Param("country_id") String country_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                UPDATE media SET belong_to_collection_id = :belong_to_collection_id
                WHERE cinevo_id = :cinevo_id;
            """)
    void updateBelongToCollectionId(@Param("cinevo_id") String cinevo_id, @Param("belong_to_collection_id") String belong_to_collection_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                UPDATE episode SET production_country_id = :production_country_id
                WHERE cinevo_id = :episode_cinevo_id;
            """)
    void updateEpisodeWithProductionCode(@Param("episode_cinevo_id") String episode_cinevo_id,
            @Param("production_country_id") String production_country_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                UPDATE episode_to_air SET production_country_id = :production_country_id
                WHERE cinevo_id = :episode_to_air_cienvo_id
            """)
    void updateEpisodeToAirWithProductionCountry(@Param("episode_to_air_cienvo_id") String episode_to_air_cienvo_id,
            @Param("production_country_id") String production_country_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO media_production_country(media_id, production_country_id)
                VALUES(:media_id, :production_country_id)
            """)
    void updateOrInsertMediaProductionCountry(@Param("media_id") String media_id, @Param("production_country_id") String production_country_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO media_production_company(media_id, production_company_id)
                VALUES(:media_id, :production_company_id)
            """)
    void updateOrInsertMediaProductionCompany(@Param("media_id") String media_id, @Param("production_company_id") String production_company_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO media_spoken_language(media_id, spoken_language_id)
                VALUES(:media_id, :spoken_language_id)
            """)
    void updateOrInsertMediaSpokenLanguage(@Param("media_id") String media_id, @Param("spoken_language_id") String spoken_language_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO media_network(media_id, network_id)
                VALUES(:media_id, :network_id)
            """)
    void updateOrInsertMediaNetwork(@Param("media_id") String media_id, @Param("network_id") String network_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO media_genre(media_id, genre_id)
                VALUES (:media_id, :genre_id)
            """)
    void updateOrInsertMediaGenre(@Param("media_id") String media_id, @Param("genre_id") String genre_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO season(cinevo_id, last_update, tmdb_id, air_date, episode_count, `name`, overview, poster_path, season_number, vote_average, media_id)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :air_date, :episode_count, :name, :overview, :poster_path, :season_number, :vote_average, :media_id)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    air_date = VALUES(air_date),
                    episode_count = VALUES(episode_count),
                    `name` = VALUES(`name`),
                    overview = VALUES(overview),
                    poster_path = VALUES(poster_path),
                    season_number = VALUES(season_number),
                    vote_average = VALUES(vote_average),
                    media_id = VALUES(media_id)
            """)
    void updateOrInsertSeason(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("air_date") LocalDate air_date,
            @Param("episode_count") Integer episode_count, @Param("name") String name, @Param("overview") String overview,
            @Param("poster_path") String poster_path, @Param("season_number") Integer season_number, @Param("vote_average") Integer vote_average,
            @Param("media_id") String media_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO episode(cinevo_id, last_update, tmdb_id, air_date, episode_number, episode_type, overview, runtime, still_path, vote_average, vote_count, season_id)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :air_date, :episode_number, :episode_type, :overview, :runtime, :still_path, :vote_average,:vote_count, :season_id)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    air_date = VALUES(air_date),
                    episode_number = VALUES(episode_number),
                    episode_type = VALUES(episode_type),
                    overview = VALUES(overview),
                    runtime = VALUES(runtime),
                    still_path = VALUES(still_path),
                    vote_average = VALUES(vote_average),
                    vote_count = VALUES(vote_count),
                    season_id = VALUES(season_id)
            """)
    void updateOrInsertEpisode(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("air_date") LocalDate air_date,
            @Param("episode_number") Integer episode_number, @Param("episode_type") String episode_type, @Param("overview") String overview,
            @Param("runtime") Integer runtime, @Param("still_path") String still_path, @Param("vote_average") Double vote_average,
            @Param("vote_count") Integer vote_count, @Param("season_id") String season_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO episode_to_air(cinevo_id, last_update, tmdb_id, `name`, overview, vote_average, vote_count, air_date, episode_number,
                episode_type, runtime, season_number, still_path, media_id)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :name, :overview, :vote_average, :vote_count, :air_date, :episode_number, :episode_type, :runtime,
                :season_number, :still_path, :media_id)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    `name` = VALUES(`name`),
                    overview = VALUES(overview),
                    vote_average = VALUES(vote_average),
                    vote_count = VALUES(vote_count),
                    air_date = VALUES(air_date),
                    episode_number = VALUES(episode_number),
                    episode_type = VALUES(episode_type),
                    runtime = VALUES(runtime),
                    season_number = VALUES(season_number),
                    still_path = VALUES(still_path),
                    media_id = VALUES(media_id)
            """)
    void updateOrInsertEpisodeToAir(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("name") String name,
            @Param("overview") String overview, @Param("vote_average") Integer vote_average, @Param("vote_count") Integer vote_count,
            @Param("air_date") LocalDate air_date, @Param("episode_number") Integer episode_number, @Param("episode_type") String episode_type,
            @Param("runtime") Integer runtime, @Param("season_number") Integer season_number, @Param("still_path") String still_path,
            @Param("media_id") String media_id);
}
