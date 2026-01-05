package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Network;
import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NetworkRepository extends JpaRepository<Network, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Network.TABLE_AS + ".* FROM " + Network.TABLE_NAME + Network.TABLE_AS + " WHERE " + Network.TMDB_ID + " = :tmdb_id")
    Optional<Network> getNetworkByTmdbId(@Param("tmdb_id") final Integer tmdb_id);
}
