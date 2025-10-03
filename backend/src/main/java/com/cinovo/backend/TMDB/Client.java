package com.cinovo.backend.TMDB;

import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.DTO.*;
import com.cinovo.backend.TMDB.Util.AbstractHttpClient;
import com.cinovo.backend.TMDB.Util.ApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@JBossLog
public class Client extends AbstractHttpClient {
    private final String COLLECTION = "collection";
    private final String CONFIGURATION = "configuration";

    public Client(ApiProperties apiProperties) {
        super(apiProperties);
    }

    protected GenresResponse getGenres(final Type type) throws Exception {
        String json = sendGet("genre/" + (type == Type.MOVIE ? "movie" : "tv") + "/list", null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, GenresResponse.class);
    }

    protected CertificationResponse getCertification(final Type type) throws Exception {
        String json = sendGet("certification/" + (type == Type.MOVIE ? "movie" : "tv") + "/list", null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, CertificationResponse.class);
    }

    protected TranslationResponse getTranslation(final Integer id) throws Exception {
        String json = sendGet(String.format(COLLECTION + "/%s/translations", id), null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, TranslationResponse.class);
    }

    protected ImageResponse getImage(final Integer id) throws Exception {
        String json = sendGet(String.format(COLLECTION + "/%s/images", id), null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ImageResponse.class);
    }

    protected CollectionDetailsResponse getCollectionDetail(final Integer id) throws Exception {
        String json = sendGet(String.format(COLLECTION + "/%s?language=en-US", id), null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, CollectionDetailsResponse.class);
    }

    protected CompanyDetailsResponse getCompanyDetail(final Integer id) throws Exception {
        String json = sendGet(String.format("company/%s", id), null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, CompanyDetailsResponse.class);
    }

    protected ConfigurationTimezonesResponse[] getConfigurationTimezones() throws Exception {
        String json = sendGet(CONFIGURATION + "/timezones", null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ConfigurationTimezonesResponse[].class);
    }

    protected String[] getConfigurationPrimaryTranslations() throws Exception {
        String json = sendGet(CONFIGURATION + "/primary_translations", null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, String[].class);
    }

    protected ConfigurationLanguageResponse[] getConfigurationLanguages() throws Exception {
        String json = sendGet(CONFIGURATION + "/languages", null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ConfigurationLanguageResponse[].class);
    }

    protected ConfigurationCountryResponse[] getConfigurationCountries(Map<String, String> map) throws Exception {
        String json = sendGet(CONFIGURATION + "/countries", map).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ConfigurationCountryResponse[].class);
    }

    protected ConfigurationJobsResponse[] getConfigurationJobs() throws Exception {
        String json = sendGet(CONFIGURATION + "/jobs", null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ConfigurationJobsResponse[].class);
    }

    protected ConfigurationDetailsResponse getConfigurationDetails() throws Exception {
        String json = sendGet(CONFIGURATION , null).body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ConfigurationDetailsResponse.class);
    }
}
