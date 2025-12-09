package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Translate;
import com.cinovo.backend.DB.Repository.TranslateRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.TranslationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TranslateService implements TMDBLogically<Integer, List<Translate>>
{
    private final TranslateRepository translateRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public TranslateService(TranslateRepository translateRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.translateRepository = translateRepository;
        this.service = service;
    }

    public List<Translate> findAllTranslateById(final Integer id) throws Exception
    {
        Optional<List<Translate>> translates = this.translateRepository.findAllTranslateById(id);
        if(translates.isEmpty() || translates.get().isEmpty())
        {
            return this.onConvertTMDB(id);
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
            Translate trans = this.translateRepository.findByIdAndIso(translationResponse.getId(), translate.getIso_639_1()).orElse(new Translate());
            trans.setId(translationResponse.getId());
            trans.setIso_upper(translate.getIso_3166_1());
            trans.setIso_lower(translate.getIso_639_1());
            trans.setName(translate.getName());
            trans.setEnglish_name(translate.getEnglish_name());
            trans.setTitle(translate.getData().getTitle());
            trans.setOverview(translate.getData().getOverview());
            trans.setHome_page(translate.getData().getHomepage());
            translates.add(trans);
        }

        this.translateRepository.saveAll(translates);
        return translates;
    }

}
