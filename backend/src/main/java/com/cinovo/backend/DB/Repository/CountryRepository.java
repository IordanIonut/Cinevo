package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Country.TABLE_AS + ".* FROM " + Country.TABLE_NAME + Country.TABLE_AS + " WHERE " + Country.TYPE + " = :media_type")
    Optional<List<Country>> findCountryByMediaType(@Param("media_type") String media_type);

    @Query(nativeQuery = true, value = "SELECT " + Country.TABLE_AS + ".* FROM " + Country.TABLE_NAME + Country.TABLE_AS + " WHERE " + Country.TYPE
            + " = :media_type AND " + Country.CODE + " = :code")
    Optional<Country> findCountryByMediaTypeAndCode(@Param("media_type") String media_type, @Param("code") String code);

    @Query(nativeQuery = true,
            value = "SELECT " + Country.Certification.TABLE_AS + ".* FROM " + Country.Certification.TABLE_NAME + Country.Certification.TABLE_AS
                    + " WHERE " + Country.Certification.COUNTRY_CINEVO_ID + " = :country_id")
    Optional<Country.Certification> findCountryCertificationByCountryCinevoId(@Param("country_id") String country_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO country (cinevo_id, last_update, code, `type`)
            VALUES(:cinevo_id, NOW(), :code, :type)
            ON DUPLICATE KEY UPDATE
                last_update = NOW(),
                code = VALUES(code),
                `type` = VALUES(`type`)
            """)
    void updateOrInsertCountry(@Param("cinevo_id") String cinevo_id, @Param("code") String code, @Param("type") String type);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO certification(cinevo_id, last_update, certification, meaning, `order`, country_cinevo_id)
            VALUES(:cinevo_id, NOW(), :certification, :meaning, :order, :country_cinevo_id)
            ON DUPLICATE KEY UPDATE
                last_update = NOW(),
                certification = VALUES(certification),
                meaning = VALUES(meaning),
                `order` = VALUES(`order`),
                country_cinevo_id = VALUES(country_cinevo_id)
            """)
    void updateOrInsertCertification(@Param("cinevo_id") String cinevo_id, @Param("certification") String certification,
            @Param("meaning") String meaning, @Param("order") Integer order, @Param("country_cinevo_id") String country_cinevo_id);
}
