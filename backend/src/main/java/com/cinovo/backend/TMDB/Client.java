package com.cinovo.backend.TMDB;

import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.GenresResponse;
import com.cinovo.backend.TMDB.Util.AbstractHttpClient;
import com.cinovo.backend.TMDB.Util.ApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@JBossLog
public class Client extends AbstractHttpClient
{
    private ObjectMapper mapper;
    private final String COLLECTION = "collection";
    private final String CONFIGURATION = "configuration";
    private final String DISCOVER = "discover";
    private final String KEYWORDS = "keyword";
    private final String MOVIE = "movie";

    public Client(ApiProperties apiProperties)
    {
        super(apiProperties);
        this.mapper = new ObjectMapper();
    }

    protected GenresResponse getGenres(final Type type) throws Exception
    {
        String json = sendGet("genre/" + (type == Type.MOVIE ? "movie" : "tv") + "/list", null).body();
        return mapper.readValue(json, GenresResponse.class);
    }

    protected CertificationResponse getCertification(final Type type) throws Exception
    {
        String json = sendGet("certification/" + (type == Type.MOVIE ? "movie" : "tv") + "/list", null).body();
        return mapper.readValue(json, CertificationResponse.class);
    }

    protected TranslationResponse getTranslation(final Integer id) throws Exception
    {
        String json = sendGet(String.format(COLLECTION + "/%s/translations", id), null).body();
        return mapper.readValue(json, TranslationResponse.class);
    }

    protected CollectionImageResponse getImageCollection(final Integer id) throws Exception
    {
        String json = sendGet(String.format(COLLECTION + "/%s/images", id), null).body();
        return mapper.readValue(json, CollectionImageResponse.class);
    }

    protected CollectionDetailsResponse getCollectionDetail(final Integer id) throws Exception
    {
        String json = sendGet(String.format(COLLECTION + "/%s?language=en-US", id), null).body();
        return mapper.readValue(json, CollectionDetailsResponse.class);
    }

    protected CompanyDetailsResponse getCompanyDetail(final Integer id) throws Exception
    {
        String json = sendGet(String.format("company/%s", id), null).body();
        return mapper.readValue(json, CompanyDetailsResponse.class);
    }

    protected ConfigurationTimezonesResponse[] getConfigurationTimezones() throws Exception
    {
        String json = sendGet(CONFIGURATION + "/timezones", null).body();
        return mapper.readValue(json, ConfigurationTimezonesResponse[].class);
    }

    protected String[] getConfigurationPrimaryTranslations() throws Exception
    {
        String json = sendGet(CONFIGURATION + "/primary_translations", null).body();
        return mapper.readValue(json, String[].class);
    }

    protected ConfigurationLanguageResponse[] getConfigurationLanguages() throws Exception
    {
        String json = sendGet(CONFIGURATION + "/languages", null).body();
        return mapper.readValue(json, ConfigurationLanguageResponse[].class);
    }

    protected ConfigurationCountryResponse[] getConfigurationCountries(Map<String, String> map) throws Exception
    {
        String json = sendGet(CONFIGURATION + "/countries", map).body();
        return mapper.readValue(json, ConfigurationCountryResponse[].class);
    }

    protected ConfigurationJobsResponse[] getConfigurationJobs() throws Exception
    {
        String json = sendGet(CONFIGURATION + "/jobs", null).body();
        return mapper.readValue(json, ConfigurationJobsResponse[].class);
    }

    protected ConfigurationDetailsResponse getConfigurationDetails() throws Exception
    {
        String json = sendGet(CONFIGURATION, null).body();
        return mapper.readValue(json, ConfigurationDetailsResponse.class);
    }

    protected DetailsResponse getCreditsDetails(final String id) throws Exception
    {
        String json = sendGet("credit/" + id, null).body();
        return mapper.readValue(json, DetailsResponse.class);
    }

    protected DiscoverMovieResponse getDiscoverMovie() throws Exception
    {
        String json = sendGet(DISCOVER + "/movie", null).body();
        return mapper.readValue(json, DiscoverMovieResponse.class);
    }

    protected DiscoverTVResponse getDiscoverTV() throws Exception
    {
        String json = sendGet(DISCOVER + "/tv", null).body();
        return mapper.readValue(json, DiscoverTVResponse.class);
    }

    protected KeywordsDetailResponse getKeywordDetails(final Integer id) throws Exception
    {
        String json = sendGet(KEYWORDS + "/" + id, null).body();
        return mapper.readValue(json, KeywordsDetailResponse.class);
    }

    protected KeywordMovieResponse getKeywordMovieDetails(final Integer id, Map<String, String> map) throws Exception
    {
        String json = sendGet(KEYWORDS + "/" + id + "/movies", map).body();
        return mapper.readValue(json, KeywordMovieResponse.class);
    }

    protected NowPlayingResponse getMovieNowPlaying(Map<String, String> map) throws Exception
    {
        String json = sendGet(MOVIE + "/now_playing", map).body();
        return mapper.readValue(json, NowPlayingResponse.class);
    }

    protected PopularResponse getMoviePopular(Map<String, String> map) throws Exception
    {
        String json = sendGet(MOVIE + "/popular", map).body();
        return mapper.readValue(json, PopularResponse.class);
    }

    protected TopRatedResponse getTopRated(Map<String, String> map) throws Exception
    {
        String json = sendGet(MOVIE + "/top_rated", map).body();
        return mapper.readValue(json, TopRatedResponse.class);
    }

    protected UpComingResponse getUnComing(Map<String, String> map) throws Exception
    {
        String json = sendGet(MOVIE + "/upcoming", map).body();
        return mapper.readValue(json, UpComingResponse.class);
    }

    protected CreditResponse getMovieCredit(final Integer id, Map<String, String> map) throws Exception
    {
        String json = sendGet(MOVIE + "/" + id + "/credits", map).body();
        return mapper.readValue(json, CreditResponse.class);
    }

    protected MovieDetailsResponse getMovieDetails(final Integer id, Map<String, String> map) throws Exception
    {
        String json = sendGet(MOVIE + "/" + id, map).body();
        return mapper.readValue(json, MovieDetailsResponse.class);
    }

    protected MovieExternalIdsResponse getMovieExternalIds(final Integer id) throws Exception
    {
        String json = sendGet(MOVIE + "/" + id + "/external_ids", null).body();
        return mapper.readValue(json, MovieExternalIdsResponse.class);
    }

    protected MovieImagesResponse getMovieImages(final Integer id) throws Exception
    {
        String json = sendGet(MOVIE + "/" + id + "/images", null).body();
        return mapper.readValue(json, MovieImagesResponse.class);
    }
}
