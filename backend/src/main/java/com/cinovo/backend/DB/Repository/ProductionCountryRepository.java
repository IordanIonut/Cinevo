package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.ProductionCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionCountryRepository extends JpaRepository<ProductionCountry, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + ProductionCountry.TABLE_AS + ".* FROM " + ProductionCountry.TABLE_NAME + ProductionCountry.TABLE_AS + " WHERE "
                    + ProductionCountry.ISO + " = :iso")
    Optional<ProductionCountry> getProductionCountryById(@Param("iso") String iso);

    @Query(nativeQuery = true,
            value = "SELECT " + ProductionCountry.TABLE_AS + ".* FROM " + ProductionCountry.TABLE_NAME + ProductionCountry.TABLE_AS)
    Optional<List<ProductionCountry>> findAllProductionCountry();
}
