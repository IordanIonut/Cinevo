package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.External;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ExternalRepository extends JpaRepository<External, String>
{
    @Query(nativeQuery = true, value = "SELECT e.* FROM external e WHERE e.person_id = :fk_cinevo_id OR e.media_id = :fk_cinevo_id OR e.season_id = :fk_cinevo_id OR e.episode_id = :fk_cinevo_id")
    Optional<External> findByFindCinevoIdOnFK(@Param("fk_cinevo_id")String fk_cinevo_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO external(cinevo_id, last_update, imdb_id, freebase_mid, freebase_id, tvdb_id, tvrage_id, wikidata_id, 
                facebook_id, instagram_id, twitter_id, youtube_id, tiktok_id, media_id, person_id, season_id, episode_id)
            VALUES(:cinevo_id, NOW(), :imdb_id, :freebase_mid, :freebase_id, :tvdb_id, :tvrage_id, :wikidata_id, 
                :facebook_id, :instagram_id, :twitter_id, :youtube_id, :tiktok_id, :media_id, :person_id, :season_id, :episode_id)
            ON DUPLICATE KEY UPDATE
                last_update = NOW(),
                imdb_id = VALUES(imdb_id),
                freebase_mid = VALUES(freebase_mid),
                freebase_id = VALUES(freebase_id),
                tvdb_id = VALUES(tvdb_id),
                tvrage_id = VALUES(tvrage_id),
                wikidata_id = VALUES(wikidata_id),
                facebook_id = VALUES(facebook_id),
                instagram_id = VALUES(instagram_id),
                twitter_id = VALUES(twitter_id),
                youtube_id = VALUES(youtube_id),
                tiktok_id = VALUES(tiktok_id),
                media_id = VALUES(media_id),
                person_id = VALUES(person_id),
                season_id = VALUES(season_id),
                episode_id = VALUES(episode_id)
            """)
    void updateOrInsertExternal(@Param("cinevo_id") String cinevo_id, @Param("imdb_id") String imdb_id, @Param("freebase_mid") String freebase_mid,
            @Param("freebase_id") String freebase_id, @Param("tvdb_id") Integer tvdb_id, @Param("tvrage_id") String tvrage_id,
            @Param("wikidata_id") String wikidata_id, @Param("facebook_id") String facebook_id, @Param("instagram_id") String instagram_id,
            @Param("twitter_id") String twitter_id, @Param("youtube_id") String youtube_id, @Param("tiktok_id") String tiktok_id,
            @Param("media_id") String media_id, @Param("person_id") String person_id, @Param("season_id") String season_id,
            @Param("episode_id") String episode_id);
}

