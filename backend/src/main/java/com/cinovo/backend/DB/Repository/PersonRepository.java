package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Person.TABLE_AS + ".* FROM " + Person.TABLE_NAME + Person.TABLE_AS + " WHERE " + Person.TMDB_ID + " = :tmdb_id")
    Optional<Person> findByTmdbId(@Param("tmdb_id") final Integer tmdb_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO  person(cinevo_id, last_update, tmdb_id, adult, gender, known_for_department, original_name, `name`, profile_file, popularity, biography, birthday, deathday, homepage,
                    freebase_mid, freebase_id, imdb_id, tvrage_id, wikidata_id, facebook_id, twitter_id, youtube_id, instagram_id, tiktok_id, place_of_birth)
                VALUES(:cinevo_id, NOW(), :tmdb_id, :adult, :gender, :known_for_department, :original_name, :name, :profile_file, :popularity, :biography, :birthday, :deathday, :homepage, 
                    :freebase_mid, :freebase_id, :imdb_id, :tvrage_id, :wikidata_id, :facebook_id, :twitter_id, :youtube_id, :instagram_id, :tiktok_id, :place_of_birth)
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
                    freebase_mid = VALUES(freebase_mid),
                    freebase_id = VALUES(freebase_id),
                    imdb_id = VALUES(imdb_id),
                    tvrage_id = VALUES(tvrage_id),
                    wikidata_id = VALUES(wikidata_id),
                    facebook_id = VALUES(facebook_id),
                    twitter_id = VALUES(twitter_id),
                    youtube_id = VALUES(youtube_id),
                    instagram_id = VALUES(instagram_id),
                    tiktok_id = VALUES(tiktok_id),
                    place_of_birth = VALUES(place_of_birth)
            """)
    void updateOrInsertPerson(@Param("cinevo_id") String cinevo_id, @Param("tmdb_id") Integer tmdb_id, @Param("adult") Boolean adult,
            @Param("gender") String gender, @Param("known_for_department") String known_for_department, @Param("original_name") String original_name,
            @Param("name") String name, @Param("profile_file") String profile_file, @Param("popularity") Double popularity,
            @Param("biography") String biography, @Param("birthday") LocalDate birthday, @Param("deathday") LocalDate deathday,
            @Param("homepage") String homepage, @Param("freebase_mid") String freebase_mid, @Param("freebase_id") String freebase_id,
            @Param("imdb_id") String imdb_id, @Param("tvrage_id") String tvrage_id, @Param("wikidata_id") String wikidata_id,
            @Param("facebook_id") String facebook_id, @Param("twitter_id") String twitter_id, @Param("youtube_id") String youtube_id,
            @Param("instagram_id") String instagram_id, @Param("tiktok_id") String tiktok_id, @Param("place_of_birth") String place_of_birth);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO person_also_know_as(also_know_as, person_cinevo_id)
            VALUES(:also_know_as, :person_cinevo_id)
            ON DUPLICATE KEY UPDATE 
                also_know_as = VALUES(also_know_as)
            """)
    void updateOrUpdatePersonAlsoKnowAs(@Param("also_know_as") String also_know_as, @Param("person_cinevo_id") String person_cinevo_id);
}