package com.cinovo.backend.TMDB;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.*;
import com.cinovo.backend.TMDB.Util.AbstractHttpClient;
import com.cinovo.backend.TMDB.Util.ApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Component;

import javax.print.attribute.HashPrintJobAttributeSet;
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
    private final String SEASON = "season";
    private final String EPISODE = "episode";
    private final String CHANGES = "changes";

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

    protected SearchResponse<MediaResponse> getDiscoverMovie() throws Exception
    {
        return this.generateGetCall(DISCOVER + "/movie", null, SearchResponse.class);
    }

    protected SearchResponse<MediaResponse> getDiscoverTV() throws Exception
    {
        return this.generateGetCall(DISCOVER + "/tv", null, SearchResponse.class);
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

    protected MediaExternalIdResponse getMovieExternalIds(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/external_ids", null, MediaExternalIdResponse.class);
    }

    protected MediaImagesResponse getMovieImages(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/images", null, MediaImagesResponse.class);
    }

    protected MediaKeywordResponse getKeywordMovie(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/keywords", null, MediaKeywordResponse.class);
    }

    protected SearchResponse<MediaResponse> getMediaRecommendation(final Integer id, final MediaType type, HashMap<String, String> map)
            throws Exception
    {
        return this.generateGetCall((type.equals(MediaType.MOVIE) ? MOVIE : TV) + "/" + id + "/recommendations", map, SearchResponse.class);
    }

    protected MovieReleaseDateResponse getMovieReleaseDate(final Integer id) throws Exception
    {
        return this.generateGetCall(MOVIE + "/" + id + "/release_dates", null, MovieReleaseDateResponse.class);
    }

    protected SearchResponse<MediaResponse> getMediaSimilar(final Integer id, final MediaType type, HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall((type.equals(MediaType.MOVIE) ? MOVIE : TV) + "/" + id + "/similar", map, SearchResponse.class);
    }

    protected MediaVideoResponse getMediaVideo(final Integer id, final MediaType type, HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall((type.equals(MediaType.MOVIE) ? MOVIE : TV) + "/" + id + "/videos", map, MediaVideoResponse.class);
    }

    protected MediaWatchProvidersResponse getMediaWatchProvider(final Integer id, final MediaType type) throws Exception
    {
        return this.generateGetCall((type.equals(MediaType.MOVIE) ? MOVIE : TV) + "/" + id + "/watch/providers", null,
                MediaWatchProvidersResponse.class);
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
        return this.generateGetCall(
                TRENDING + (type == null ? "/all" : MediaType.MOVIE.name().equals(type.name()) ? "/movie" : "/tv") + "/" + time_window, map,
                SearchResponse.class);
    }

    protected SearchResponse<PeopleResponse> getTrendingPeopleResponse(final String time_window, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TRENDING + "/person/" + time_window, map, SearchResponse.class);
    }

    protected MediaResponse getTvDetails(final Integer id, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + id, map, MediaResponse.class);
    }

    protected CreditResponse getTvAggregateCredits(final Integer id, Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + id + "/aggregate_credits", map, CreditResponse.class);
    }

    protected MediaExternalIdResponse getTvExternalIds(final Integer id) throws Exception
    {
        return this.generateGetCall(TV + "/" + id + "/external_ids", null, MediaExternalIdResponse.class);
    }

    protected MediaImagesResponse getTvImages(final Integer id, Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + id + "/images", map, MediaImagesResponse.class);
    }

    protected MediaKeywordResponse getTvKeyword(final Integer id) throws Exception
    {
        return this.generateGetCall(TV + "/" + id + "/keywords", null, MediaKeywordResponse.class);
    }

    protected TvSeasonDetailsResponse getTvSeasonDetails(final Integer series_id, final Integer series_number, final Map<String, String> map)
            throws Exception
    {
        return this.generateGetCall(TV + "/" + series_id + "/" + SEASON + "/" + series_number, map, TvSeasonDetailsResponse.class);
    }

    protected CreditResponse getTvSeasonCredit(final Integer series_id, final Integer series_number, final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + series_id + "/" + SEASON + "/" + series_number + "/aggregate_credits", map, CreditResponse.class);
    }

    protected MediaExternalIdResponse getTvSeasonExternalId(final Integer series_id, final Integer series_number) throws Exception
    {
        return this.generateGetCall(TV + "/" + series_id + "/" + SEASON + "/" + series_number + "/external_ids", null, MediaExternalIdResponse.class);
    }

    protected MediaWatchProvidersResponse getTvSeasonMediaWatchProvider(final Integer series_id, final Integer series_number,
            final Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + series_id + "/" + SEASON + "/" + series_number + "/watch/providers", map,
                MediaWatchProvidersResponse.class);
    }

    protected MediaVideoResponse getTvSeasonMediaVideo(final Integer series_id, final Integer season_number, Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + series_id + "/" + SEASON + "/" + season_number + "/videos", map, MediaVideoResponse.class);
    }

    protected MediaImagesResponse getTvSeasonMediaImage(final Integer series_id, final Integer season_number, Map<String, String> map)
            throws Exception
    {
        return this.generateGetCall(TV + "/" + series_id + "/" + SEASON + "/" + season_number + "/images", map, MediaImagesResponse.class);
    }

    protected MediaExternalIdResponse getTvSeasonEpisodeExternal(final Integer series_id, final Integer season_number, final Integer episode_number)
            throws Exception
    {
        return this.generateGetCall(
                TV + "/" + series_id + "/" + SEASON + "/" + season_number + "/" + EPISODE + "/" + episode_number + "/external_ids", null,
                MediaExternalIdResponse.class);
    }

    protected MediaImagesResponse getTvSeasonEpisodeImage(final Integer series_id, final Integer season_number, final Integer episode_number,
            final HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + series_id + "/" + SEASON + "/" + season_number + "/" + EPISODE + "/" + episode_number + "/images", map,
                MediaImagesResponse.class);
    }

    protected MediaVideoResponse getTvSeasonEpisodeVideo(final Integer series_id, final Integer season_number, final Integer episode_number,
            Map<String, String> map) throws Exception
    {
        return this.generateGetCall(TV + "/" + series_id + "/" + SEASON + "/" + season_number + "/" + EPISODE + "/" + episode_number + "/videos", map,
                MediaVideoResponse.class);
    }

    protected ChangesResponse getChangeResponse(final MediaType type, HashMap<String, String> map) throws Exception
    {
        return this.generateGetCall(
                (type == MediaType.MOVIE ? "movie" : type == MediaType.TV ? "tv" : type == MediaType.PERSON ? "person" : "") + "/changes", map,
                ChangesResponse.class);
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
