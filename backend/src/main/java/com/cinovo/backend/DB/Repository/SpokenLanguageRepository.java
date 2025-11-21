package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.SpokenLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpokenLanguageRepository extends JpaRepository<SpokenLanguage, String>
{

    @Query(nativeQuery = true,
            value = "SELECT " + SpokenLanguage.TABLE_AS + ".* FROM " + SpokenLanguage.TABLE_NAME + SpokenLanguage.TABLE_AS + " WHERE "
                    + SpokenLanguage.ISO + " = :iso")
    Optional<SpokenLanguage> getSpokenLanguageById(@Param("iso") String iso);

    @Query(nativeQuery = true, value = "SELECT " + SpokenLanguage.TABLE_AS + ".* FROM " + SpokenLanguage.TABLE_NAME + SpokenLanguage.TABLE_AS)
    Optional<List<SpokenLanguage>> findAllSpokenLanguages();
}
