package com.cinovo.backend.TMDB;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.*;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    public GenresResponse getGenres(final MediaType type) throws Exception
    {
        return this.client.getGenres(type);
    }

    public CertificationResponse getCertification(final MediaType type) throws Exception
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

    public CollectionResponse getCollectionDetail(final Integer id) throws Exception
    {
        return this.client.getCollectionDetail(id);
    }

    public CompanyResponse getCompanyDetail(final Integer id) throws Exception
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

    //TODO: not use
    public DetailsResponse getCreditsDetails(final String id) throws Exception
    {
        return this.client.getCreditsDetails(id);
    }

    public SearchResponse getDiscoverMovie() throws Exception
    {
        return this.client.getDiscoverMovie();
    }

    public SearchResponse getDiscoverTV() throws Exception
    {
        return this.client.getDiscoverTV();
    }

    public KeywordsResponse getKeywordDetails(final Integer id) throws Exception
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

    public MediaResponse getMovieDetails(final Integer id, final String language) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getMovieDetails(id, map);
    }

    public MediaExternalIdResponse getMovieExternalIds(final Integer id) throws Exception
    {
        return this.client.getMovieExternalIds(id);
    }

    public MediaImagesResponse getMovieImages(final Integer id) throws Exception
    {
        return this.client.getMovieImages(id);
    }

    public MediaKeywordResponse getKeywordMovie(final Integer id) throws Exception
    {
        return this.client.getKeywordMovie(id);
    }

    public SearchResponse<MediaResponse> getMediaRecommendation(final Integer id, final MediaType type, final String language, final Integer page)
            throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);
        map.put("page", page + "");
        return this.client.getMediaRecommendation(id, type, map);
    }

    public MovieReleaseDateResponse getMovieReleaseDate(final Integer id) throws Exception
    {
        return this.client.getMovieReleaseDate(id);
    }

    public SearchResponse<MediaResponse> getMediaSimilar(final Integer id, final MediaType type, final String language, final Integer page)
            throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);
        map.put("page", page + "");
        return this.client.getMediaSimilar(id, type, map);
    }

    public MediaVideoResponse getMediaVideo(final Integer id, final MediaType type, final String include_video_language, final String language)
            throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);
        map.put("include_video_language", include_video_language);

        return this.client.getMediaVideo(id, type, map);
    }

    public MediaWatchProvidersResponse getMediaWatchProvider(final Integer id, final MediaType type) throws Exception
    {
        return this.client.getMediaWatchProvider(id, type);
    }

    public NetworkDetailResponse getNetworkDetail(final Integer id) throws Exception
    {
        return this.client.getNetworkDetail(id);
    }

    public NetworkAlternativeNameResponse getNetworkAlternativeName(final Integer id) throws Exception
    {
        return this.client.getNetworkAlternativeName(id);
    }

    public NetworkImageResponse getNetworkImage(final Integer id) throws Exception
    {
        return this.client.getNetworkImage(id);
    }

    public PeoplePopularResponse getPeoplePopular(final String language, final Integer page) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);
        map.put("page", page + "");
        return this.client.getPeoplePopular(map);
    }

    public PeopleResponse getPeopleDetail(final Integer id, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getPeopleDetail(id, map);
    }

    public CreditResponse getPeopleCombinedCredit(final Integer id, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getPeopleCombinedCredit(id, map);
    }

    public PersonExternalIdsResponse getPersonExternalIds(final Integer id) throws Exception
    {
        return this.client.getPersonExternalIds(id);
    }

    public PeopleImagesResponse getPeopleImages(final Integer id) throws Exception
    {
        return this.client.getPeopleImages(id);
    }

    public CreditResponse getPeopleMovieCast(final Integer id, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getPeopleMovieCast(id, map);
    }

    public CreditResponse getPeopleTvCast(final Integer id, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);
        return this.client.getPeopleTvCast(id, map);
    }

    public SearchResponse<CollectionResponse> getSearchCollectionResponse(final String query, final Boolean include_adult, final String language,
            final Integer page, final String region) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("query", query);
        map.put("language", language);
        map.put("include_adult", include_adult + "");
        map.put("page", page + "");
        map.put("region", region);

        return this.client.getSearchCollectionResponse(map);
    }

    public SearchResponse<CompanyResponse> getSearchCompanyResponse(final String query, final Integer page) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("query", query);
        map.put("page", page + "");

        return this.client.getSearchCompanyResponse(map);
    }

    public SearchResponse<KeywordsResponse> getSearchKeywordsResponse(final String query, final Integer page) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("query", query);
        map.put("page", page + "");

        return this.client.getSearchKeywordsResponse(map);
    }

    public SearchResponse<MediaResponse> getSearchMediaResponse(final MediaType type, final String query, final Boolean include_adult,
            final String language, final String primary_release_year, final Integer page, final String region, final Integer year) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("query", query);
        map.put("include_adult", include_adult + "");
        map.put("language", language);
        map.put("primary_release_year", primary_release_year);
        map.put("page", page + "");
        map.put("region", region);
        map.put("year", year + "");

        return this.client.getSearchMediaResponse(type, map);
    }

    public SearchResponse<PeopleResponse> getSearchPeopleResponse(final String query, final Boolean include_adult, final String language,
            final Integer page) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("query", query);
        map.put("include_adult", include_adult + "");
        map.put("language", language);
        map.put("page", page + "");

        return this.client.getSearchPeopleResponse(map);
    }

    public SearchResponse<MediaResponse> getTrendingMediaResponse(final MediaType type, final String time_window, final String language)
            throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);

        return this.client.getTrendingMediaResponse(type, time_window, map);
    }

    public SearchResponse<PeopleResponse> getTrendingPeopleResponse(final String time_window, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);

        return this.client.getTrendingPeopleResponse(time_window, map);
    }

    public MediaResponse getTvDetails(final Integer id, final String append_to_response, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("append_to_response", append_to_response);
        map.put("language", language);

        return this.client.getTvDetails(id, map);
    }

    public CreditResponse getTvAggregateCredits(final Integer id, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);

        return this.client.getTvAggregateCredits(id, map);
    }

    public MediaExternalIdResponse getTvExternalIds(final Integer id) throws Exception
    {
        return this.client.getTvExternalIds(id);
    }

    public MediaImagesResponse getTvImages(final Integer id, final String include_image_language, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("include_image_language", include_image_language);
        map.put("language", language);

        return this.client.getTvImages(id, map);
    }

    public MediaKeywordResponse getTvKeyword(final Integer id) throws Exception
    {
        return this.client.getTvKeyword(id);
    }

    public TvSeasonDetailsResponse getTvSeasonDetails(final Integer series_id, final Integer series_number, final String append_to_response,
            final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("append_to_response", append_to_response);
        map.put("language", language);

        return this.client.getTvSeasonDetails(series_id, series_number, map);
    }

    public CreditResponse getTvSeasonCredit(final Integer series_id, final Integer series_number, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);

        return this.client.getTvSeasonCredit(series_id, series_number, map);
    }

    public MediaExternalIdResponse getTvSeasonExternalId(final Integer series_id, final Integer series_number) throws Exception
    {
        return this.client.getTvSeasonExternalId(series_id, series_number);
    }

    public MediaWatchProvidersResponse getTvSeasonMediaWatchProvider(final Integer series_id, final Integer series_number, final String language)
            throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("language", language);

        return this.client.getTvSeasonMediaWatchProvider(series_id, series_number, map);
    }

    public MediaVideoResponse getTvSeasonMediaVideo(final Integer series_id, final Integer series_number, final String include_video_language,
            final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("include_video_language", include_video_language);
        map.put("language", language);

        return this.client.getTvSeasonMediaVideo(series_id, series_number, map);
    }

    public MediaImagesResponse getTvSeasonMediaImage(final Integer series_id, final Integer series_number, final String include_video_language,
            final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("include_video_language", include_video_language);
        map.put("language", language);

        return this.client.getTvSeasonMediaImage(series_id, series_number, map);
    }

    public MediaExternalIdResponse getTvSeasonEpisodeExternal(final Integer series_id, final Integer season_number, final Integer episode_number)
            throws Exception
    {
        return this.client.getTvSeasonEpisodeExternal(series_id, season_number, episode_number);
    }

    public MediaImagesResponse getTvSeasonEpisodeImage(final Integer series_id, final Integer season_number, final Integer episode_number,
            final String include_image_language, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("include_image_language", include_image_language);
        map.put("language", language);

        return this.client.getTvSeasonEpisodeImage(series_id, season_number, episode_number, map);
    }

    public MediaVideoResponse getTvSeasonEpisodeVideo(final Integer series_id, final Integer season_number, final Integer episode_number,
            final String include_image_language, final String language) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("include_image_language", include_image_language);
        map.put("language", language);

        return this.client.getTvSeasonEpisodeVideo(series_id, season_number, episode_number, map);
    }

    public ChangesResponse getChangeResponse(final MediaType type, final Date end_date, final Integer page, final Date start_date) throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("end_date", end_date == null ? null : end_date.toString());
        map.put("page", page+"");
        map.put("start_date", start_date == null ? null : start_date.toString());

        return this.client.getChangeResponse(type, map);
    }
}
