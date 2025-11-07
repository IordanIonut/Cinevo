package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Media.TABLE_AS + ".*" + " FROM " + Media.TABLE_NAME + Media.TABLE_AS + " WHERE " + Media.ID + " = :id AND "
                    + Media.TYPE + " = :type")
    Optional<Media> getMediaByIdAndType(@Param("id") final Integer id, @Param("type") final String type);
}
