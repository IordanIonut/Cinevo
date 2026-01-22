package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.ProductionCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionCountryRepository extends JpaRepository<ProductionCountry, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + ProductionCountry.TABLE_AS + ".* FROM " + ProductionCountry.TABLE_NAME + ProductionCountry.TABLE_AS + " WHERE "
                    + ProductionCountry.ISO + " = :iso")
    Optional<ProductionCountry> getByIso(@Param("iso") String iso);

    @Query(nativeQuery = true,
            value = "SELECT " + ProductionCountry.TABLE_AS + ".* FROM " + ProductionCountry.TABLE_NAME + ProductionCountry.TABLE_AS)
    Optional<List<ProductionCountry>> findAllProductionCountry();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO production_country(cinevo_id, last_update, ISO_3166_1,  `name`)
                VALUES(:cinevo_id, NOW(), :ISO_3166_1, :name)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    ISO_3166_1 = VALUES(ISO_3166_1),
                    `name` = VALUES(`name`)
            """)
    void updateOrInsert(@Param("cinevo_id") String cinevo_id, @Param("ISO_3166_1") String ISO_3166_1, @Param("name") String name);
}
