package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.WatchProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchProviderRepository extends JpaRepository<WatchProvider, String>
{
    @Query(value = "SELECT " + WatchProvider.TABLE_AS + ".* FROM " + WatchProvider.TABLE_NAME + WatchProvider.TABLE_AS + " JOIN " + Media.TABLE_NAME
            + Media.TABLE_AS + " ON " + WatchProvider.JOIN_MEDIA + " = " + Media.CINEVO_ID + " WHERE " + Media.ID + " = :movie_id",
            nativeQuery = true)
    Optional<List<WatchProvider>> findWatchProviderByMovieId(@Param("movie_id") final Integer movie_id);
}
