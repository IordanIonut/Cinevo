package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Translate;
import com.cinovo.backend.DB.Repository.TranslateRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.TranslationResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@JBossLog
public class TranslateService implements TMDBLogically<Integer, List<Translate>>
{
    private final TranslateRepository translateRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public TranslateService(TranslateRepository translateRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.translateRepository = translateRepository;
        this.service = service;
    }

    public List<Translate> findByTmdbId(final Integer tmdb_id) throws Exception
    {
        Optional<List<Translate>> translates = this.translateRepository.findByTmdbId(tmdb_id);
        if(translates.isEmpty() || translates.get().isEmpty())
        {
            return this.onConvertTMDB(tmdb_id);
        }
        return translates.get();
    }

    @Override
    @Transactional
    public List<Translate> onConvertTMDB(Integer id) throws Exception
    {
        List<Translate> translates = new ArrayList<>();
        TranslationResponse translationResponse = this.service.getTranslate(id);

        for(TranslationResponse.Translate translate : translationResponse.getTranslations())
        {
            this.translateRepository.updateOrInsert(
                    Shared.generateCinevoId(this.translateRepository.findByTmdbIdAndIso(translationResponse.getId(), translate.getIso_639_1())),
                    translationResponse.getId(), translate.getIso_3166_1(), translate.getIso_639_1(), translate.getName(),
                    translate.getEnglish_name(), translate.getData().getTitle(), translate.getData().getOverview(),
                    translate.getData().getHomepage());

            translates.add(this.translateRepository.findByTmdbIdAndIso(translationResponse.getId(), translate.getIso_3166_1()).get());
        }

        return this.translateRepository.findAll();
    }

}
