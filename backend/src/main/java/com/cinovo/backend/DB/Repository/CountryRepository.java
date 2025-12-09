package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Country.TABLE_AS + ".* FROM " + Country.TABLE_NAME + Country.TABLE_AS + " WHERE " + Country.TYPE + " = :type")
    Optional<List<Country>> findCountryByType(@Param("type") String type);

    @Query(nativeQuery = true,
            value = "SELECT " + Country.TABLE_AS + ".* FROM " + Country.TABLE_NAME + Country.TABLE_AS + " WHERE " + Country.TYPE + " = :type AND "
                    + Country.CODE + " = :code")
    Optional<Country> findCountryByTypeAndCode(@Param("type") String type, @Param("code") String code);

    @Query(nativeQuery = true,
            value = "SELECT " + Country.Certification.TABLE_AS + ".* FROM " + Country.Certification.TABLE_NAME + Country.Certification.TABLE_AS
                    + " WHERE " + Country.Certification.COUNTRY_CINEVO_ID + " = :country_id")
    Optional<Country.Certification> findCountryCertificationByCountryCinevoId(@Param("country_id") String country_id);
}
