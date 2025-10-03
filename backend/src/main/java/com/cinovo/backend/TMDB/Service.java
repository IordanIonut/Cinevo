package com.cinovo.backend.TMDB;


import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.DTO.*;
import org.springframework.stereotype.Component;

import java.lang.module.Configuration;
import java.util.HashMap;
import java.util.Map;

@Component
@org.springframework.stereotype.Service
public class Service {
    private final Client client;

    public Service(Client client) {
        this.client = client;
    }

    public GenresResponse getGenres(final Type type) throws Exception {
        return this.client.getGenres(type);
    }

    public CertificationResponse getCertification(final Type type) throws Exception {
        return this.client.getCertification(type);
    }

    public TranslationResponse getTranslate(final Integer id) throws Exception {
        return this.client.getTranslation(id);
    }

    public ImageResponse getImage(final Integer id) throws Exception {
        return this.client.getImage(id);
    }

    public CollectionDetailsResponse getCollectionDetail(final Integer id) throws Exception {
        return this.client.getCollectionDetail(id);
    }

    public CompanyDetailsResponse getCompanyDetail(final Integer id) throws Exception {
        return this.client.getCompanyDetail(id);
    }

    public ConfigurationTimezonesResponse[] getConfigurationTimezones() throws Exception {
        return this.client.getConfigurationTimezones();
    }

    public String[] getConfigurationPrimaryTranslations() throws Exception {
        return this.client.getConfigurationPrimaryTranslations();
    }

    public ConfigurationLanguageResponse[] getConfigurationLanguages() throws Exception {
        return this.client.getConfigurationLanguages();
    }

    public ConfigurationCountryResponse[] getConfigurationCountries(final String language) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getConfigurationCountries(map);
    }

    public ConfigurationJobsResponse[] getConfigurationJobs() throws Exception {
        return this.client.getConfigurationJobs();
    }

    public ConfigurationDetailsResponse getConfigurationDetails() throws Exception {
        return this.client.getConfigurationDetails();
    }
}
