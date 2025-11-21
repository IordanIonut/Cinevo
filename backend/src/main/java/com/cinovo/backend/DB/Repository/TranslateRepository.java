package com.cinovo.backend.DB.Repository;

import com.cinovo.backend.DB.Model.Translate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslateRepository extends JpaRepository<Translate, String>
{
    @Query(value = "SELECT " + Translate.TABLE_AS + ".* FROM " + Translate.TABLE_NAME + Translate.TABLE_AS + " WHERE " + Translate.ID + " = :id",
            nativeQuery = true)
    Optional<List<Translate>> findAllTranslateById(@Param("id") final Integer id);

    @Query(value = "SELECT" + Translate.TABLE_AS + ".* FROM" + Translate.TABLE_NAME + Translate.TABLE_AS + " WHERE " + Translate.ID + " = :id"
            + Translate.ISO_UPPER + " = :iso", nativeQuery = true)
    Optional<Translate> findByIdAndIso(@Param("id") final Integer id, @Param("iso") final String iso);
}
