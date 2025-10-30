package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.Movie;
import com.cinovo.backend.Enum.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String>
{
    @Query(value = "SELECT " + Image.TABLE_AS + ".* FROM " + Image.TABLE_NAME + Image.TABLE_AS + " JOIN " + Movie.TABLE_NAME + Movie.TABLE_AS + " ON "
            + Image.JOIN_MOVIE + " = " + Movie.CINEVO_ID + " WHERE ( 'MOVIE' = :image_type AND " + Movie.ID + " = :id)",
            nativeQuery = true)
    Optional<List<Image>> findImageByIdAndType(@Param("id") final Integer id, @Param("image_type") final String image_type);
}
