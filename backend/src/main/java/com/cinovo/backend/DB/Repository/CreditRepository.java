package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Credit.TABLE_AS + ".* FROM " + Credit.TABLE_NAME + Credit.TABLE_AS + " JOIN " + Media.TABLE_NAME + Media.TABLE_AS
                    + "  ON " + Credit.JOIN_MEDIA + " WHERE " + Media.TMDB_ID + " = :media_tmdb_id")
    Optional<List<Credit>> findByMediaTmdbId(@Param("media_tmdb_id") final Integer media_tmdb_id);

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
                    + " ON " + Credit.PERSON_ID + " = " + Person.CINEVO_ID + " WHERE " + Person.TMDB_ID + " = :person_tmdb_id")
    Optional<List<Credit>> findByPersonTmdbId(@Param("person_tmdb_id") final Integer person_tmdb_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO credit(cinevo_id, last_update, credit_type, cast_id, `character`, credit_id, `order`, episode_count, first_credit_air_date, media_id, person_id)
                VALUES(:cinevo_id, NOW(), :credit_type, :cast_id, :character, :credit_id, :order, :episode_count, :first_credit_air_date, :media_id, :person_id)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    credit_type = VALUES(credit_type),
                    cast_id = VALUES(cast_id),
                    `character` = VALUES(`character`),
                    credit_id = VALUES(credit_id),
                    `order` = VALUES(`order`),
                    episode_count = VALUES(episode_count),
                    first_credit_air_date = VALUES(first_credit_air_date),
                    media_id = VALUES(media_id),
                    person_id = VALUES(person_id)
            """)
    void updateOrInsertCredit(@Param("cinevo_id") String cinevo_id, @Param("credit_type") String credit_type, @Param("cast_id") Integer cast_id,
            @Param("character") String character, @Param("credit_id") String credit_id, @Param("order") Integer order,
            @Param("episode_count") Integer episode_count, @Param("first_credit_air_date") LocalDate first_credit_air_date,
            @Param("media_id") String media_id, @Param("person_id") String person_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO credit_department(credit_cinevo_id, department)
                VALUES(:credit_cinevo_id, :department)
            """)
    void updateOrInsertCreditDepartment(@Param("credit_cinevo_id") String credit_cinevo_id, @Param("department") String department);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO credit_position(credit_cinevo_id, position)
                VALUES (:credit_cinevo_id, :position)
            """)
    void updateOrInsertCreditPosition(@Param("credit_cinevo_id") String credit_cinevo_id, @Param("position") String position);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO job(cinevo_id, last_update, credit_id, job, episode_count)
            VALUES(:cinevo_id, NOW(), :credit_id, :job, :episode_count)
            ON DUPLICATE KEY UPDATE
                last_update = NOW(),
                credit_id = VALUES(credit_id),
                job = VALUES(job),
                episode_count = VALUES(episode_count)
            """)
    void updateOrInsertCreditJob(@Param("cinevo_id") String cinevo_id, @Param("credit_id") String credit_id, @Param("job") String job,
            @Param("episode_count") Integer episode_count);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                 INSERT IGNORE INTO credit_job(credit_cinevo_id, job_cinevo_id)
                 VALUES(:credit_cinevo_id, :job_cinevo_id)
            """)
    void updateOrInsertCreditJobList(@Param("credit_cinevo_id") String credit_cinevo_id, @Param("job_cinevo_id") String job_cinevo_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
             INSERT INTO role(cinevo_id, last_update, credit_id, `character`, episode_count)
             VALUES(:cinevo_id, NOW(), credit_id, :character, :episode_count)
             ON DUPLICATE KEY UPDATE
                 last_update = NOW(),
                 credit_id = VALUES(credit_id),
                 `character` = VALUES(`character`),
                 episode_count = VALUES(episode_count)
            """)
    void updateOrInsertCreditRole(@Param("cinevo_id") String cinevo_id, @Param("credit_id") String credit_id, @Param("character") String character,
            @Param("episode_count") Integer episode_count);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                 INSERT IGNORE INTO credit_role(credit_cinevo_id, role_cinevo_id)
                 VALUES(:credit_cinevo_id, :role_cinevo_id)
            """)
    void updateOrInsertCreditRoleList(@Param("credit_cinevo_id") String credit_cinevo_id, @Param("role_cinevo_id") String role_cinevo_id);
}

