package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String>
{
    @Query(nativeQuery = true,
            value = "SELECT " + Person.TABLE_AS + ".* FROM " + Person.TABLE_NAME + Person.TABLE_AS + " WHERE " + Person.TMDB_ID + " = :tmdb_id")
    Optional<Person> findByTmdbId(@Param("tmdb_id") final Integer tmdb_id);
}