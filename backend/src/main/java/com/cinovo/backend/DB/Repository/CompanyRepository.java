package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Company.TABLE_AS + ".* FROM " + Company.TABLE_NAME + Company.TABLE_AS + " WHERE " + Company.TMDB_ID + " = :tmdb_id")
    Optional<Company> findCompanyByTmdbId(@Param("tmdb_id") final Integer tmdb_id);

    @Modifying
    @Transactional
    @Query(value = """
             INSERT INTO company (cinevo_id, last_update, tmdb_id, description, headquarters, homepage, logo_path, name, origin_country, parent_company_cinevo_id)
             VALUES (:cinevo_id, NOW(), :tmdb_id, :description, :headquarters, :homepage, :logoPath, :name, :originCountry, :parentCompany)
             ON DUPLICATE KEY UPDATE 
                description = VALUES(description),
                headquarters = VALUES(headquarters),
                homepage = VALUES(homepage),
                logo_path = VALUES(logo_path),
                `name` = VALUES(`name`),
                origin_country = VALUES(origin_country),
                last_update = NOW()
            """, nativeQuery = true)
    void upsertOrInsert(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("description") String description,
            @Param("headquarters") String headquarters, @Param("homepage") String homepage, @Param("logoPath") String logoPath,
            @Param("name") String name, @Param("originCountry") String originCountry, @Param("parentCompany") String parentCompanyId);

}
