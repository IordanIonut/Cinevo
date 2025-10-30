package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.Key;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, String> {
    @Query(value = "SELECT " + Keyword.TABLE_AS + ".* FROM " + Keyword.TABLE_NAME + Keyword.TABLE_AS + " WHERE " + Keyword.ID + " = :id", nativeQuery = true)
    Optional<Keyword> findKeywordById(@Param("id") final Integer id);

}
