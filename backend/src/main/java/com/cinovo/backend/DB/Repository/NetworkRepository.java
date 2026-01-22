package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Network;
import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface NetworkRepository extends JpaRepository<Network, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Network.TABLE_AS + ".* FROM " + Network.TABLE_NAME + Network.TABLE_AS + " WHERE " + Network.TMDB_ID + " = :tmdb_id")
    Optional<Network> getNetworkByTmdbId(@Param("tmdb_id") final Integer tmdb_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO network(cinevo_id, last_update, tmdb_id, headquarters, homepage, logo_path, `name`, origin_country)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :headquarters, :homepage, :logo_path, :name, :origin_country)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    headquarters = VALUES(headquarters),
                    homepage = VALUES(homepage),
                    logo_path = VALUES(logo_path),
                    `name` = VALUES(`name`),
                    origin_country = VALUES(origin_country)
            """)
    void updateOrInsertNetwork(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("headquarters") String headquarters,
            @Param("homepage") String homepage, @Param("logo_path") String logo_path, @Param("name") String name,
            @Param("origin_country") String origin_country);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO network_alternative_name(cinevo_id, last_update, `name`, `type`, network_id)
                VALUES(:cinevo_id, NOW(), :name, :type, :network_id)
                ON DUPLICATE KEY UPDATE
                        last_update = NOW(),
                        `name` = VALUES(`name`),
                        `type` = VALUES(`type`)
            """)
    void updateOrInsertNetworkAlternativeName(@Param("cinevo_id") String cinevo_id, @Param("name") String name, @Param("type") String type,
            @Param("network_id") String network_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO network_image(cinevo_id, last_update, tmdb_id, aspect_ratio, file_path, height, width, file_type, vote_average, vote_count, network_id)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :aspect_ratio, :file_path, :height, :width, :file_type, :vote_average, :vote_count, :network_id)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    aspect_ratio = VALUES(aspect_ratio),
                    file_path = VALUES(file_path),
                    height = VALUES(height),
                    width = VALUES(width),
                    file_type = VALUES(file_type),
                    vote_average = VALUES(vote_average),
                    vote_count = VALUES(vote_count)
            """)
    void updateOrInsertNetworkImage(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") String tmdb_id,
            @Param("aspect_ratio") Double aspect_ratio, @Param("file_path") String file_path, @Param("height") Integer height,
            @Param("width") Integer width, @Param("file_type") String file_type, @Param("vote_average") Double vote_average,
            @Param("vote_count") Integer vote_count, @Param("network_id") String network_id);
}
