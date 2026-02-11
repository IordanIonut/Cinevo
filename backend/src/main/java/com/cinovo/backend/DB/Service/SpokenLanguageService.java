package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.SpokenLanguage;
import com.cinovo.backend.DB.Model.View.SpokenLanguageView;
import com.cinovo.backend.DB.Repository.SpokenLanguageRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.ConfigurationLanguageResponse;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpokenLanguageService implements TMDBLogically<Null, List<SpokenLanguage>>
{
    private final SpokenLanguageRepository spokenLanguageRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public SpokenLanguageService(SpokenLanguageRepository spokenLanguageRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.spokenLanguageRepository = spokenLanguageRepository;
        this.service = service;
    }

    public SpokenLanguage getByIso(final String iso)
    {
        return this.spokenLanguageRepository.getByIso(iso).orElse(new SpokenLanguage());
    }

    public List<SpokenLanguage> findAllSpokenLanguages() throws Exception
    {
        Optional<List<SpokenLanguage>> spokenLanguages = this.spokenLanguageRepository.findAllSpokenLanguages();
        if(spokenLanguages.isEmpty() || spokenLanguages.get().isEmpty())
        {
            return (List<SpokenLanguage>) onConvertTMDB(null);
        }
        return spokenLanguages.get();
    }

    public List<SpokenLanguageView> getSpokenLanguageViewByMediaType(final MediaType media_type)
    {
        Optional<List<SpokenLanguageView>> spokenLanguages = this.spokenLanguageRepository.getSpokenLanguageViewByMediaType(media_type.name());
        if(spokenLanguages.isEmpty() || spokenLanguages.get().isEmpty())
        {
            return new ArrayList<>();
        }
        return spokenLanguages.get();
    }

    @Override
    @Transactional
    public List<SpokenLanguage> onConvertTMDB(Null input) throws Exception
    {
        ConfigurationLanguageResponse[] spokenLanguage = this.service.getConfigurationLanguages();
        List<SpokenLanguage> spokenLanguages = new ArrayList<>();
        for(ConfigurationLanguageResponse c : spokenLanguage)
        {
            this.spokenLanguageRepository.updateOrInsert(Shared.generateCinevoId(this.spokenLanguageRepository.getByIso(c.getIso_639_1())),
                    c.getEnglish_name(), c.getIso_639_1(), c.getName());
            spokenLanguages.add(this.spokenLanguageRepository.getByIso(c.getIso_639_1()).get());
        }

        return spokenLanguages;
    }
}
