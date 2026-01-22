package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Model.View.ImageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String>
{
    @Query(nativeQuery = true, value = """
            SELECT image.*
            FROM IMAGE image
            LEFT JOIN MEDIA media on image.MEDIA_ID = media.CINEVO_ID
            LEFT JOIN  SEASON season on image.SEASON_ID = season.CINEVO_ID
            LEFT JOIN EPISODE episode on image.EPISODE_ID = episode.CINEVO_ID
            LEFT JOIN PERSON person on image.PERSON_ID = person.CINEVO_ID
            WHERE image.TYPE = :media_type
              AND (
                    (
                        :media_type IN ('MOVIE', 'TV')
                        AND media.TMDB_ID = :tmdb_id
                    )
                    OR (
                        :media_type = 'TV_SEASON'
                        AND season.TMDB_ID = :tmdb_id
                    )
                    OR (
                        :media_type = 'TV_EPISODE'
                        AND episode.TMDB_ID = :tmdb_id
                    )
                    OR (
                        :media_type = 'PERSON'
                        AND person.TMDB_ID = :tmdb_id
                    )
                  );
            """)
    Optional<List<Image>> findByTmdbIdAndMediaType(@Param("tmdb_id") final Integer tmdb_id, @Param("media_type") final String media_type);

    @Query(nativeQuery = true, value = """
              SELECT image.*
                        FROM IMAGE image
                        LEFT JOIN MEDIA media on image.MEDIA_ID = media.CINEVO_ID
                        LEFT JOIN  SEASON season on image.SEASON_ID = season.CINEVO_ID
                        LEFT JOIN EPISODE episode on image.EPISODE_ID = episode.CINEVO_ID
                        LEFT JOIN PERSON person on image.PERSON_ID = person.CINEVO_ID
                        WHERE image.TYPE = :media_type and image.file_path = :file_path
                          AND (
                                (
                                    :media_type IN ('MOVIE', 'TV')
                                    AND media.TMDB_ID = :tmdb_id
                                )
                                OR (
                                    :media_type = 'TV_SEASON'
                                    AND season.TMDB_ID = :tmdb_id
                                )
                                OR (
                                    :media_type = 'TV_EPISODE'
                                    AND episode.TMDB_ID = :tmdb_id
                                )
                                OR (
                                    :media_type = 'PERSON'
                                    AND person.TMDB_ID = :tmdb_id
                                )
                              );
            """)
    Optional<Image> findByTmdbIdAndTypeAndFilePath(@Param("tmdb_id") final Integer tmdb_id, @Param("media_type") final String media_type,
            @Param("file_path") String file_path);

    @Query(nativeQuery = true,
            value = "SELECT " + Image.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + Image.TABLE_NAME + Image.TABLE_AS
                    + " ON " + Image.MEDIA_ID + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Media.Season.CINEVO_ID + " = " + Image.SEASON_ID + " WHERE " + Media.TMDB_ID + " = :media_tmdb_id AND "
                    + Media.Season.SEASON_NUMBER + " = :season_number ")
    Optional<List<Image>> findImageBySeasonIdAndSeasonNumber(@Param("media_tmdb_id") final Integer media_tmdb_id,
            @Param("season_number") final Integer season_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Image.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + Image.TABLE_NAME + Image.TABLE_AS
                    + " ON " + Image.MEDIA_ID + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Media.Season.CINEVO_ID + " = " + Image.SEASON_ID + " JOIN " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " ON " + Media.Season.CINEVO_ID + " = " + Media.Season.Episode.SEASON_ID + " WHERE " + Media.TMDB_ID + " = :media_tmdb_id AND "
                    + Media.Season.SEASON_NUMBER + " = :season_number AND " + Media.Season.Episode.EPISODE_NUMBER + " = :episode_number")
    Optional<List<Image>> findByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(@Param("media_tmdb_id") final Integer media_tmdb_id,
            @Param("season_number") final Integer season_number, @Param("episode_number") final Integer episode_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Image.TABLE_AS + ".* FROM " + Image.TABLE_NAME + Image.TABLE_AS + " WHERE " + Image.MEDIA_ID + " = :media_id AND "
                    + Image.IMAGE_TYPE + " = :image_type AND " + Image.TYPE + " = :type AND " + Image.COLLECTION_ID + " = :collection_id AND "
                    + Image.FILE_PATH + " = :file_path")
    Optional<Image> findByMediaIdAndCollectionIdAndImageTypeAndTypeAndFilePath(@Param("media_id") final String media_id,
            @Param("image_type") final String image_type, @Param("collection_id") final Integer collection_id, @Param("type") final String type,
            @Param("file_path") final String file_path);

    @Query(nativeQuery = true, value = "SELECT i.* FROM IMAGE i WHERE i.cinevo_id = :cinevo_id")
    Optional<Image> findByCinevoId(@Param("cinevo_id") String cinevo_id);

    @Query(nativeQuery = true, value = """
                SELECT
                    CONCAT(:url, i.file_path) AS file_path,
                    i.cinevo_id AS cinevo_id,
                    i.image_type AS type,
                    i.vote_average AS vote_average
                FROM image i
                    LEFT JOIN episode e ON
                        i.episode_id = e.cinevo_id
                    LEFT JOIN season s ON
                        i.season_id = s.cinevo_id OR  e.season_id = s.cinevo_id
                    LEFT JOIN media m ON
                    	m.cinevo_id = i.media_id OR m.cinevo_id = s.media_id
                    LEFT JOIN person p ON
                        p.cinevo_id = i.person_id
                WHERE
                   (:media_type IN ('MOVIE', 'TV') AND m.cinevo_id = :cinevo_id  AND i.image_type NOT IN ('LOGO', 'BACKDROP', 'PROFILE')) 
                    OR (:media_type = 'PERSON' AND p.cinevo_id = :cinevo_id)
                ORDER BY i.vote_average DESC limit 10;
            """)
    Optional<List<ImageView>> findImageViewByMediaTypeAndCinevoId(@Param("url") String url, @Param("media_type") String media_type,
            @Param("cinevo_id") String cinevo_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO image(cinevo_id, last_update, `type`, aspect_radio, height, iso_3166_1, iso_639_1, file_path, 
                        vote_average, vote_count, width, image_type, collection_id, media_id, season_id, episode_id, person_id)
            VALUES(:cinevo_id, NOW(), :type, :aspect_radio, :height, :iso_3166_1, :iso_639_1, :file_path, 
                        :vote_average, :vote_count, :width, :image_type, :collection_id, :media_id, :season_id, :episode_id, :person_id )
            ON DUPLICATE KEY UPDATE 
                    last_update = NOW(),
                    `type` = VALUES(`type`),
                    aspect_radio = VALUES(aspect_radio),
                    height = VALUES(height),
                    iso_3166_1 = VALUES(iso_3166_1),
                    iso_639_1 = VALUES(iso_639_1),
                    file_path = VALUES(file_path),
                    vote_average = VALUES(vote_average),
                    vote_count = VALUES(vote_count),
                    width = VALUES(width),
                    image_type = VALUES(image_type),
                    collection_id = VALUES(collection_id),
                    media_id = VALUES(media_id),
                    season_id = VALUES(season_id),
                    episode_id = VALUES(episode_id),
                    person_id = VALUES(person_id)
            """)
    void updateOrInsert(@Param("cinevo_id") String cinevo_id, @Param("type") String type, @Param("aspect_radio") Double aspect_radio,
            @Param("height") Integer height, @Param("iso_3166_1") String iso_3166_1, @Param("iso_639_1") String iso_639_1,
            @Param("file_path") String file_path, @Param("vote_average") Double vote_average, @Param("vote_count") Integer vote_count,
            @Param("width") Integer width, @Param("image_type") String image_type, @Param("collection_id") Integer collection_id,
            @Param("media_id") String media_id, @Param("season_id") String season_id, @Param("episode_id") String episode_id,
            @Param("person_id") String person_id);
}
