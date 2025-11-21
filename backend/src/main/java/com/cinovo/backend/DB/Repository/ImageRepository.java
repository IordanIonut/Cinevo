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
    @Query(nativeQuery = true,
            value = "SELECT " + Image.TABLE_AS + ".* FROM " + Image.TABLE_NAME + Image.TABLE_AS + " JOIN " + Media.TABLE_NAME + Media.TABLE_AS
                    + " ON " + Image.MEDIA_ID + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Image.SEASON_ID + " = " + Media.Season.CINEVO_ID + " JOIN " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " ON " + Image.EPISODE_ID + " = " + Media.Season.Episode.CINEVO_ID + " JOIN " + Person.TABLE_NAME + Person.TABLE_AS + " ON "
                    + Image.PERSON_ID + " = " + Person.CINEVO_ID + " WHERE " + Image.TYPE
                    + " = :media_type  AND (('MOVIE' = :media_type || 'TV' = :media_type) AND " + Media.ID
                    + " = :id) AND ('TV_SEASON' = :media_type AND " + Media.Season.ID + " = :id) AND ('TV_EPISODE' = :media_type AND "
                    + Media.Season.Episode.ID + " = :id) AND ('PERSON' = :media_type AND " + Person.ID + " = :id )")
    Optional<List<Image>> findImageByMediaIdAndMediaType(@Param("id") final Integer id, @Param("media_type") final String media_type);

    @Query(nativeQuery = true,
            value = "SELECT " + Image.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + Image.TABLE_NAME + Image.TABLE_AS
                    + " ON " + Image.MEDIA_ID + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Media.Season.CINEVO_ID + " = " + Image.SEASON_ID + " WHERE " + Media.ID + " = :series_id AND " + Media.Season.SEASON_NUMBER
                    + " = :season_number ")
    Optional<List<Image>> findImageBySeasonIdAndSeasonNumber(@Param("series_id") final Integer series_id,
            @Param("season_number") final Integer season_number);

    @Query(nativeQuery = true,
            value = "SELECT " + Image.TABLE_AS + ".* FROM " + Media.TABLE_NAME + Media.TABLE_AS + " JOIN " + Image.TABLE_NAME + Image.TABLE_AS
                    + " ON " + Image.MEDIA_ID + " = " + Media.CINEVO_ID + " JOIN " + Media.Season.TABLE_NAME + Media.Season.TABLE_AS + " ON "
                    + Media.Season.CINEVO_ID + " = " + Image.SEASON_ID + " JOIN " + Media.Season.Episode.TABLE_NAME + Media.Season.Episode.TABLE_AS
                    + " ON " + Media.Season.CINEVO_ID + " = " + Media.Season.Episode.SEASON_ID + " WHERE " + Media.ID + " = :series_id AND "
                    + Media.Season.SEASON_NUMBER + " = :season_number AND " + Media.Season.Episode.EPISODE_NUMBER + " = :episode_number")
    Optional<List<Image>> findImageBySeasonIdAndSeasonNumberAndEpisodeAndMediaType(@Param("series_id") final Integer series_id,
            @Param("season_number") final Integer season_number, @Param("episode_number") final Integer episode_number);

    @Query(value = "SELECT " + Image.TABLE_AS + ".* FROM " + Image.TABLE_NAME + Image.TABLE_AS + " WHERE " + Image.MEDIA_ID + " = :media_id AND "
            + Image.IMAGE_TYPE + " = :image_type AND " + Image.TYPE + " = :type AND " + Image.COLLECTION_ID + " = :collection_id", nativeQuery = true)
    Optional<Image> findByMediaIdAndCollectionIdAndImageTypeAndType(@Param("media_id") final String media_id,
            @Param("image_type") final String image_type, @Param("collection_id") final Integer collection_id, @Param("type") final String type);
}
