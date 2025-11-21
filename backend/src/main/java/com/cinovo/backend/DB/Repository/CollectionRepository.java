package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Collection.TABLE_AS + ".* FROM " + Collection.TABLE_NAME + Collection.TABLE_AS + " WHERE " + Collection.ID + " = :id")
    Optional<Collection> findCollectionById(@Param("id") Integer id);

}
