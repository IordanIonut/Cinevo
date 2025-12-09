package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.SpokenLanguage;
import com.cinovo.backend.DB.Repository.SpokenLanguageRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.ConfigurationLanguageResponse;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public SpokenLanguage getSpokenLanguageById(final String iso)
    {
        return this.spokenLanguageRepository.getSpokenLanguageById(iso).orElse(new SpokenLanguage());
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

    @Override
    @Transactional
    public List<SpokenLanguage> onConvertTMDB(Null input) throws Exception
    {
        ConfigurationLanguageResponse[] spokenLanguage = this.service.getConfigurationLanguages();
        List<SpokenLanguage> spokenLanguages = new ArrayList<>();
        for(ConfigurationLanguageResponse c : spokenLanguage)
        {
            SpokenLanguage spoken = this.spokenLanguageRepository.getSpokenLanguageById(c.getIso_639_1()).orElse(new SpokenLanguage());
            spoken.setEnglish_name(c.getEnglish_name());
            spoken.setIso_639_1(c.getIso_639_1());
            spoken.setName(c.getName());

            spokenLanguages.add(spoken);
        }

        this.spokenLanguageRepository.saveAll(spokenLanguages);
        return spokenLanguages;
    }
}
