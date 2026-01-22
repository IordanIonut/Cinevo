package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Model.View.PersonView;
import jakarta.validation.Valid;
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
public interface PersonRepository extends JpaRepository<Person, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Person.TABLE_AS + ".* FROM " + Person.TABLE_NAME + Person.TABLE_AS + " WHERE " + Person.TMDB_ID + " = :tmdb_id")
    Optional<Person> findByTmdbId(@Param("tmdb_id") final Integer tmdb_id);

    @Query(nativeQuery = true, value = """
            SELECT 
                    CONCAT(:url, p.PROFILE_FILE) AS profile_file,
                    p.CINEVO_ID AS cinevo_id,
                    p.NAME AS name,
                    p.KNOWN_FOR_DEPARTMENT AS known_for_department,
                    p.POPULARITY AS popularity,
                    p.HOMEPAGE as homepage
            FROM PERSON p 
            WHERE p.TMDB_ID IN (:ids)
            """)
    Optional<List<PersonView>> getPersonUsingTrending(@Param("url") String url, @Param("ids") List<Integer> ids);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO  person(cinevo_id, last_update, tmdb_id, adult, gender, known_for_department, original_name, `name`, profile_file, popularity, biography, birthday, deathday, homepage, place_of_birth)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :adult, :gender, :known_for_department, :original_name, :name, :profile_file, :popularity, :biography, :birthday, :deathday, :homepage, :place_of_birth)
                ON DUPLICATE KEY UPDATE
                    last_update = NOW(),
                    tmdb_id = VALUES(tmdb_id),
                    adult = VALUES(adult),
                    gender = VALUES(gender),
                    known_for_department = VALUES(known_for_department),
                    original_name = VALUES(original_name),
                    `name` = VALUES(`name`),
                    profile_file = VALUES(profile_file),
                    popularity = VALUES(popularity),
                    biography = VALUES(biography),
                    birthday = VALUES(birthday),
                    deathday = VALUES(deathday),
                    homepage = VALUES(homepage),
                    place_of_birth = VALUES(place_of_birth)
            """)
    void updateOrInsertPerson(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("adult") Boolean adult,
            @Param("gender") String gender, @Param("known_for_department") String known_for_department, @Param("original_name") String original_name,
            @Param("name") String name, @Param("profile_file") String profile_file, @Param("popularity") Double popularity,
            @Param("biography") String biography, @Param("birthday") LocalDate birthday, @Param("deathday") LocalDate deathday,
            @Param("homepage") String homepage, @Param("place_of_birth") String place_of_birth);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO person_also_know_as(also_know_as, person_cinevo_id)
            VALUES(:also_know_as, :person_cinevo_id)
            ON DUPLICATE KEY UPDATE 
                also_know_as = VALUES(also_know_as)
            """)
    void updateOrInsertPersonAlsoKnowAs(@Param("also_know_as") String also_know_as, @Param("person_cinevo_id") String person_cinevo_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT IGNORE INTO person_media(media_id, person_id)
                VALUES(:media_id, :person_id)
            """)
    void updateOrInsertPersonMedia(@Param("media_id") String media_id, @Param("person_id") String person_id);
}