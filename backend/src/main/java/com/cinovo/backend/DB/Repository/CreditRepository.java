package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Movie;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, String>
{
    @Query(value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " JOIN " + Movie.TABLE_NAME + Movie.TABLE_AS
            + "  ON " + Credit.JOIN_MOVIE + " WHERE " + Movie.ID + " = :movie_id", nativeQuery = true)
    Optional<List<Credit>> findCreditByMovieId(@Param("movie_id") final Integer movie_id);

    @Query(value = "SELECT " + Credit.TABLE_AS+ ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " WHERE " + Credit.CREDIT_ID + " = :credit_id", nativeQuery = true)
    Optional<Credit> findCreditByCreditId(@Param("credit_id") final String credit_id);
}
