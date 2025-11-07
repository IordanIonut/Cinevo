package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, String>
{
    @Query(value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " JOIN " + Media.TABLE_NAME + Media.TABLE_AS
            + "  ON " + Credit.JOIN_MEDIA + " WHERE " + Media.ID + " = :movie_id", nativeQuery = true)
    Optional<List<Credit>> findCreditByMovieId(@Param("movie_id") final Integer movie_id);

    @Query(value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " WHERE " + Credit.MEDIA_ID
            + " = :movie_cinevo_id AND " + Credit.PERSON_ID + " = :person_cinevo_id", nativeQuery = true)
    Optional<Credit> findCreditByMovieAndPerson(@Param("movie_cinevo_id") final String movie_cinevo_id,
            @Param("person_cinevo_id") final String person_cinevo_id);
}
