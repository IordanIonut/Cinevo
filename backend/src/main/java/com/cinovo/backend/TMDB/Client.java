package com.cinovo.backend.TMDB;

import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.*;
import com.cinovo.backend.TMDB.Util.AbstractHttpClient;
import com.cinovo.backend.TMDB.Util.ApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@JBossLog
public class Client extends AbstractHttpClient
{
    private final ObjectMapper mapper;
    private final String COLLECTION = "collection";
    private final String CONFIGURATION = "configuration";
    private final String DISCOVER = "discover";
    private final String KEYWORDS = "keyword";
    private final String MOVIE = "movie";
    private final String NETWORK = "network";
    private final String PEOPLE = "person";
    private final String SEARCH = "search";
    private final String TRENDING = "trending";
    private final String TV = "tv";

    public Client(ApiProperties apiProperties)
    {
        super(apiProperties);
        this.mapper = new ObjectMapper();
    }

    protected GenresResponse getGenres(final MediaType type) throws Exception
    {
        return this.generateGetCall("genre/" + (type == MediaType.MOVIE ? "movie" : "tv") + "/list", null, GenresResponse.class);
    }

    protected CertificationResponse getCertification(final MediaType type) throws Exception
    {
        return this.generateGetCall("certification/" + (type == MediaType.MOVIE ? "movie" : "tv") + "/list", null, CertificationResponse.class);
    }

    protected TranslationResponse getTranslation(final Integer id) throws Exception
    {
        return this.generateGetCall(String.format(COLLECTION + "/%s/translations", id), null, TranslationResponse.class);
    }

    protected CollectionImageResponse getImageCollection(final Integer id) throws Exception
    {
        return this.generateGetCall(String.format(COLLECTION + "/%s/images", id), null, CollectionImageResponse.class);
    }

    protected CollectionResponse getCollectionDetail(final Integer id) throws Exception
    {
        return this.generateGetCall(String.format(COLLECTION + "/%s?language=en-US", id), null, CollectionResponse.class);
    }

    protected CompanyResponse getCompanyDetail(final Integer id) throws Exception
    {
        return this.generateGetCall(String.format("company/%s", id), null, CompanyResponse.class);
    }

    protected ConfigurationTimezonesResponse[] getConfigurationTimezones() throws Exception
    {
        return this.generateGetCall(CONFIGURATION + "/timezones", null, ConfigurationTimezonesResponse[].class);
    }

    protected String[] getConfigurationPrimaryTranslations() throws Exception
    {
        return this.generateGetCall(CONFIGURATION + "/primary_translations", null, String[].class);
    }

    protected ConfigurationLanguageResponse[] getConfigurationLanguages() throws Exception
    {
        return this.generateGetCall(CONFIGURATION + "/languages", null, ConfigurationLanguageResponse[].class);
    }

    protected ConfigurationCountryResponse[] getConfigurationCountries(Map<String, String> map) throws Exception
    {
        return this.generateGetCall(CONFIGURATION + "/countries", null, ConfigurationCountryResponse[].class);
    }

    protected ConfigurationJobsResponse[] getConfigurationJobs() throws Exception
    {
        return this.generateGetCall(CONFIGURATION + "/jobs", null, ConfigurationJobsResponse[].class);
    }

    protected ConfigurationDetailsResponse getConfigurationDetails() throws Exception
    {
        return this.generateGetCall(CONFIGURATION, null, ConfigurationDetailsResponse.class);
    }

    protected DetailsResponse getCreditsDetails(final String id) throws Exception
    {
        return this.generateGetCall("credit/" + id, null, DetailsResponse.class);
    }

    protected RecommendationMovieResponse getDiscoverMovie() throws Exception
    {
        return this.generateGetCall(DISCOVER + "/movie", null, RecommendationMovieResponse.class);
    }

    protected DiscoverTVResponse getDiscoverTV() throws Exception
    {
        return this.generateGetCall(DISCOVER + "/tv", null, DiscoverTVResponse.class);
    }

    protected KeywordsResponse getKeywordDetails(final Integer id) throws Exception
    {
        return this.generateGetCall(KEYWORDS + "/" + id, null, KeywordsResponse.class);
    }

    protected KeywordMovieResponse getKeywordMovieDetails(final Integer id, Map<String, String> map) throws Exception
    {
        return this.generateGetCall(KEYWORDS + "/" + id + "/movies", map, KeywordMovieResponse.class);
    }

    protected NowPlayingResponse getMovieNowPlaying(Map<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/now_playing", map, NowPlayingResponse.class);
    }

    protected PopularResponse getMoviePopular(Map<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/popular", map, PopularResponse.class);
    }

    protected TopRatedResponse getTopRated(Map<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/top_rated", map, TopRatedResponse.class);
    }

    protected UpComingResponse getUnComing(Map<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/upcoming", map, UpComingResponse.class);
    }

    protected CreditResponse getMovieCredit(final Integer id, Map<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/credits", map, CreditResponse.class);
    }

    protected MediaResponse getMovieDetails(final Integer id, Map<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id, map, MediaResponse.class);
    }

    protected MovieExternalIdsResponse getMovieExternalIds(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/external_ids", null, MovieExternalIdsResponse.class);
    }

    protected MovieImagesResponse getMovieImages(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/images", null, MovieImagesResponse.class);
    }

    protected MovieKeywordResponse getKeywordMovie(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/keywords", null, MovieKeywordResponse.class);
    }

    protected RecommendationMovieResponse getMovieRecommendation(final Integer id, HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/recommendations", null, RecommendationMovieResponse.class);
    }

    protected MovieReleaseDateResponse getMovieReleaseDate(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/release_dates", null, MovieReleaseDateResponse.class);
    }

    protected RecommendationMovieResponse getMovieSimilar(final Integer id, HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/similar", null, RecommendationMovieResponse.class);
    }

    protected MovieVideoResponse getMovieVideo(final Integer id, HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/videos", null, MovieVideoResponse.class);
    }

    protected MovieWatchProvidersResponse getMovieWatchProvider(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/watch/providers", null, MovieWatchProvidersResponse.class);
    }

    protected NetworkDetailResponse getNetworkDetail(final Integer id) throws Exception
    {
        return this.generateGetCall(NETWORK + "/" + id, null, NetworkDetailResponse.class);
    }

    protected NetworkAlternativeNameResponse getNetworkAlternativeName(final Integer id) throws Exception
    {
        return this.generateGetCall(NETWORK + "/" + id + "/alternative_names", null, NetworkAlternativeNameResponse.class);
    }

    protected NetworkImageResponse getNetworkImage(final Integer id) throws Exception
    {
        return this.generateGetCall(NETWORK + "/" + id + "/images", null, NetworkImageResponse.class);
    }

    protected PeoplePopularResponse getPeoplePopular(HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall("person/popular", null, PeoplePopularResponse.class);
    }

    protected PeopleResponse getPeopleDetail(final Integer id, final HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall(PEOPLE + "/" + id, map, PeopleResponse.class);
    }

    protected CreditResponse getPeopleCombinedCredit(final Integer id, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(PEOPLE + "/" + id + "/combined_credits", map, CreditResponse.class);
    }

    protected PersonExternalIdsResponse getPersonExternalIds(final Integer id) throws Exception
    {
        return this.generateGetCall(PEOPLE + "/" + id + "/external_ids", null, PersonExternalIdsResponse.class);
    }

    protected PeopleImagesResponse getPeopleImages(final Integer id) throws Exception
    {
        return this.generateGetCall(PEOPLE + "/" + id + "/images", null, PeopleImagesResponse.class);
    }

    protected CreditResponse getPeopleMovieCast(final Integer id, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(PEOPLE + "/" + id + "/movie_credits", map, CreditResponse.class);
    }

    protected CreditResponse getPeopleTvCast(final Integer id, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(PEOPLE + "/" + id + "/tv_credits", map, CreditResponse.class);
    }

    protected SearchResponse<CollectionResponse> getSearchCollectionResponse(final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(SEARCH + "/collection", map, SearchResponse.class);
    }

    protected SearchResponse<CompanyResponse> getSearchCompanyResponse(final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(SEARCH + "/company", map, SearchResponse.class);
    }

    protected SearchResponse<KeywordsResponse> getSearchKeywordsResponse(final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(SEARCH + "/keyword", map, SearchResponse.class);
    }

    protected SearchResponse<MediaResponse> getSearchMediaResponse(final MediaType type, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(SEARCH + (type == null ? "/multi" : MediaType.MOVIE.name().equals(type.name()) ? "/movie" : "/tv"), map,
                SearchResponse.class);
    }

    protected SearchResponse<PeopleResponse> getSearchPeopleResponse(final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(SEARCH + "/person", map, SearchResponse.class);
    }

    protected SearchResponse<MediaResponse> getTrendingMediaResponse(final MediaType type, final String time_window, final Map<String, String> map)
            throws Exception
    {
        return this.generateGetCall(TRENDING + (type == null ? "/all" : MediaType.MOVIE.name().equals(type.name()) ? "/movie" : "/tv") + "/" + time_window,
                map, SearchResponse.class);
    }

    protected SearchResponse<PeopleResponse> getTrendingPeopleResponse(final String time_window, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TRENDING + "/person/" + time_window, map, SearchResponse.class);
    }

    protected MediaResponse getTvDetails(final Integer id, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + id, map, MediaResponse.class);
    }

    private <T> T generateGetCall(final String url, final Map<String, String> map, final Class<T> clazz) throws Exception
    {
        HttpResponse<String> response = sendGet(url, map);

        if(response == null || response.body() == null || response.body().isBlank())
        {
            return clazz.getDeclaredConstructor().newInstance();
        }

        return mapper.readValue(response.body(), clazz);
    }
}
