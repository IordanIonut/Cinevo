package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.CreditDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditDetailsRepository extends JpaRepository<CreditDetails, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + CreditDetails.TABLE_AS + ".* FROM " + CreditDetails.TABLE_NAME + CreditDetails.TABLE_AS + " WHERE " + CreditDetails.ID
                    + " = :id")
    Optional<CreditDetails> findCreditDetailsById(@Param("id") final String id);
}
