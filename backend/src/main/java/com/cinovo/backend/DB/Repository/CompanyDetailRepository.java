package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.CompanyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyDetailRepository extends JpaRepository<CompanyDetail, String> {
    @Query(value = "SELECT " + CompanyDetail.TABLE_AS + ".* FROM " + CompanyDetail.TABLE_NAME + CompanyDetail.TABLE_AS + " WHERE " + CompanyDetail.ID + " = :id", nativeQuery = true)
    Optional<CompanyDetail> findCompanyDetailById(@Param("id") final Integer id);
}
