package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    @Query(value = "SELECT " + Image.TABLE_AS + ".* FROM " + Image.TABLE_NAME + Image.TABLE_AS + " WHERE " + Image.ID + " = :id", nativeQuery = true)
    Optional<Image> findImageById(@Param("id") Integer id);
}
