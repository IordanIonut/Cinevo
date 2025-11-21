package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Company.TABLE_AS + ".* FROM " + Company.TABLE_NAME + Company.TABLE_AS + " WHERE " + Company.ID + " = :id")
    Optional<Company> findCompanyById(@Param("id") final Integer id);
}
