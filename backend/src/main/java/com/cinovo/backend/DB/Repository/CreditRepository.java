package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
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
    @Query(nativeQuery = true,
            value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " JOIN " + Media.TABLE_NAME + Media.TABLE_AS
                    + "  ON " + Credit.JOIN_MEDIA + " WHERE " + Media.ID + " = :movie_id")
    Optional<List<Credit>> findCreditByMediaIdAndType(@Param("movie_id") final Integer movie_id);

    @Query(nativeQuery = true, value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " WHERE " + Credit.MEDIA_ID
            + " = :movie_cinevo_id AND " + Credit.PERSON_ID + " = :person_cinevo_id")
    Optional<Credit> findCreditByMovieAndPerson(@Param("movie_cinevo_id") final String movie_cinevo_id,
            @Param("person_cinevo_id") final String person_cinevo_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Credit.Role.TABLE_AS + ".* FROM " + Credit.Role.TABLE_NAME + Credit.Role.TABLE_AS + " WHERE " + Credit.Role.CREDIT_ID
                    + " = :credit_id")
    Optional<Credit.Role> findCreditRoleByCreditId(@Param("credit_id") final String credit_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Credit.Job.TABLE_AS + ".* FROM " + Credit.Job.TABLE_NAME + Credit.Job.TABLE_AS + " WHERE " + Credit.Job.CREDIT_ID
                    + " = :credit_id")
    Optional<Credit.Job> findCreditJobByCreditId(@Param("credit_id") final String credit_id);

    @Query(nativeQuery = true, value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " WHERE " + Credit.MEDIA_ID
            + " = :media_cinevo_id AND " + Credit.PERSON_ID + " = :person_cinevo_id")
    Optional<Credit> findCreditByMediaIdAndMediaTypeAndPersonCinevoId(@Param("media_cinevo_id") final String media_cinevo_id,
            @Param("person_cinevo_id") final String person_cinevo_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " WHERE " + Credit.CINEVO_ID + " = :cinevo_id")
    Optional<Credit> findCreditByCinevoId(@Param("cinevo_id") final String cinevo_id);

    @Query(nativeQuery = true,
            value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " JOIN " + Person.TABLE_NAME + Person.TABLE_AS
                    + " ON " + Credit.PERSON_ID + " = " + Person.CINEVO_ID + " WHERE " + Person.ID + " = :person_id")
    Optional<List<Credit>> findCreditByPersonId(@Param("person_id") final Integer person_id);
}
