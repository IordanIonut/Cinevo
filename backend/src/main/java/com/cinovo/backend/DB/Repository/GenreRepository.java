package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.Enum.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Genre.TABLE_AS + ".* FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.TYPE + " = :type")
    Optional<List<Genre>> findGenresByType(@Param("type") String type);

    @Query(nativeQuery = true,
            value = "SELECT " + Genre.TABLE_AS + ".* FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.TYPE + " = :type AND " + Genre.ID
                    + " = :id")
    Optional<Genre> findGenresByIdAndType(@Param("id") final Integer id, @Param("type") final String type);

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT " + Genre.TYPE + " FROM " + Genre.TABLE_NAME + Genre.TABLE_AS + " WHERE " + Genre.ID + " IN (:ids)  LIMIT 1")
    Optional<String> findMediaTypeByIDs(@Param("ids") final List<Integer> ids);
}
