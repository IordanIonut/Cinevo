package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.SpokenLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpokenLanguageRepository extends JpaRepository<SpokenLanguage, String>
{

    @Query(nativeQuery = true,
            value = "SELECT " + SpokenLanguage.TABLE_AS + ".* FROM " + SpokenLanguage.TABLE_NAME + SpokenLanguage.TABLE_AS + " WHERE "
                    + SpokenLanguage.ISO + " = :iso")
    Optional<SpokenLanguage> getByIso(@Param("iso") String iso);

    @Query(nativeQuery = true, value = "SELECT " + SpokenLanguage.TABLE_AS + ".* FROM " + SpokenLanguage.TABLE_NAME + SpokenLanguage.TABLE_AS)
    Optional<List<SpokenLanguage>> findAllSpokenLanguages();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                 INSERT INTO spoken_language(cinevo_id, last_update, english_name, iso_639_1, `name`)
                 VALUES(:cinevo_id, NOW(), :english_name, :iso_639_1, :name)
                 ON DUPLICATE KEY UPDATE
                     last_update = NOW(),
                     english_name = VALUES(english_name),
                     iso_639_1 = VALUES(iso_639_1),
                     `name` = VALUES(`name`)
            """)
    void updateOrInsert(@Param("cinevo_id") String cinevo_id, @Param("english_name") String english_name, @Param("iso_639_1") String iso_639_1,
            @Param("name") String name);
}
