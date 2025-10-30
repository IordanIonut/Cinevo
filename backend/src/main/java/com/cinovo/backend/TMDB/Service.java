package com.cinovo.backend.TMDB;

import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.GenresResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@org.springframework.stereotype.Service
public class Service
{
    private final Client client;

    public Service(Client client)
    {
        this.client = client;
    }

    public GenresResponse getGenres(final Type type) throws Exception
    {
        return this.client.getGenres(type);
    }

    public CertificationResponse getCertification(final Type type) throws Exception
    {
        return this.client.getCertification(type);
    }

    public TranslationResponse getTranslate(final Integer id) throws Exception
    {
        return this.client.getTranslation(id);
    }

    public CollectionImageResponse getImageCollection(final Integer id) throws Exception
    {
        return this.client.getImageCollection(id);
    }

    public CollectionDetailsResponse getCollectionDetail(final Integer id) throws Exception
    {
        return this.client.getCollectionDetail(id);
    }

    public CompanyDetailsResponse getCompanyDetail(final Integer id) throws Exception
    {
        return this.client.getCompanyDetail(id);
    }

    public ConfigurationTimezonesResponse[] getConfigurationTimezones() throws Exception
    {
        return this.client.getConfigurationTimezones();
    }

    public String[] getConfigurationPrimaryTranslations() throws Exception
    {
        return this.client.getConfigurationPrimaryTranslations();
    }

    public ConfigurationLanguageResponse[] getConfigurationLanguages() throws Exception
    {
        return this.client.getConfigurationLanguages();
    }

    public ConfigurationCountryResponse[] getConfigurationCountries(final String language) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getConfigurationCountries(map);
    }

    public ConfigurationJobsResponse[] getConfigurationJobs() throws Exception
    {
        return this.client.getConfigurationJobs();
    }

    public ConfigurationDetailsResponse getConfigurationDetails() throws Exception
    {
        return this.client.getConfigurationDetails();
    }

    public DetailsResponse getCreditsDetails(final String id) throws Exception
    {
        return this.client.getCreditsDetails(id);
    }

    public DiscoverMovieResponse getDiscoverMovie() throws Exception
    {
        return this.client.getDiscoverMovie();
    }

    public DiscoverTVResponse getDiscoverTV() throws Exception
    {
        return this.client.getDiscoverTV();
    }

    public KeywordsDetailResponse getKeywordDetails(final Integer id) throws Exception
    {
        return this.client.getKeywordDetails(id);
    }

    public KeywordMovieResponse getKeywordMovieDetails(final Integer id, final Integer page, final Boolean include_adult) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("language", "en-US");
        if(include_adult != null)
        {
            map.put("include_adult", include_adult + "");
        }
        return this.client.getKeywordMovieDetails(id, map);
    }

    public NowPlayingResponse getMovieNowPlaying(final String language, final Integer page, final String region) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        map.put("page", page + "");
        map.put("region", region);
        return this.client.getMovieNowPlaying(map);
    }

    public PopularResponse getMoviePopular(final String language, final Integer page, final String region) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        map.put("page", page + "");
        map.put("region", region);
        return this.client.getMoviePopular(map);
    }

    public TopRatedResponse getTopRated(final String language, final Integer page, final String region) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        map.put("page", page + "");
        map.put("region", region);
        return this.client.getTopRated(map);
    }

    public UpComingResponse getUnComing(final String language, final Integer page, final String region) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        map.put("page", page + "");
        map.put("region", region);
        return this.client.getUnComing(map);
    }

    public CreditResponse getMovieCredit(final Integer id, final String language) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getMovieCredit(id, map);
    }

    public MovieDetailsResponse getMovieDetails(final Integer id, final String language) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getMovieDetails(id, map);
    }

    public MovieExternalIdsResponse getMovieExternalIds(final Integer id) throws Exception
    {
        return this.client.getMovieExternalIds(id);
    }

    public MovieImagesResponse getMovieImages(final Integer id) throws Exception
    {
        return this.client.getMovieImages(id);
    }
}
