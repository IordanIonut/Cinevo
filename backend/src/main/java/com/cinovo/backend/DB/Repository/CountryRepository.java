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
public interface CountryRepository extends JpaRepository<Country, String> {
    @Query(value = "SELECT " + Country.TABLE_AS + ".* FROM " + Country.TABLE_NAME + Country.TABLE_AS + " WHERE " + Country.TYPE + " = :type", nativeQuery = true)
    Optional<List<Country>> findCountryByType(@Param("type") String type);

    @Query(value = "SELECT " + Country.TABLE_AS + ".* FROM " + Country.TABLE_NAME + Country.TABLE_AS + " WHERE " + Country.TYPE + " = :type AND " + Country.CODE + " = :code" ,nativeQuery = true)
    Country findCountryByTypeAndCode(@Param("type") String type, @Param("code") String code);
}
