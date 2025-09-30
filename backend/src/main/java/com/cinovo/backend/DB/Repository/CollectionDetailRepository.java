package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.CollectionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollectionDetailRepository extends JpaRepository<CollectionDetail, String> {
    @Query(value = "SELECT " + CollectionDetail.TABLE_AS + ".* FROM " + CollectionDetail.TABLE_NAME + CollectionDetail.TABLE_AS + " WHERE " + CollectionDetail.ID + " = :id", nativeQuery = true)
    Optional<CollectionDetail> findCollectionDetailById(@Param("id") Integer id);

}
