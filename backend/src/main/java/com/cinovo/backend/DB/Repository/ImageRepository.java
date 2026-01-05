package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
