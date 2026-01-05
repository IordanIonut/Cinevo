package com.cinovo.backend.TMDB;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Service.MediaService;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.*;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/api/TMDB")
@JBossLog
public class Controller
{
    private final Service service;
    private final MediaService mediaService;

    public Controller(Service service, MediaService mediaService)
    {
        this.service = service;
        this.mediaService = mediaService;
    }

    @GetMapping("/get/genre/by")
    public ResponseEntity<GenresResponse> getGenreByType(@RequestParam("type") final MediaType type)
    {
        try
        {
            log.info("getGenreByType() - Successful.....");
            return ResponseEntity.ok(this.service.getGenres(type));
        }
        catch(Exception e)
        {
            log.error("Error in getGenreByType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/certification/by")
    public ResponseEntity<CertificationResponse> getCertificationByType(@RequestParam("type") final MediaType type)
    {
        try
        {
            log.info("getCertificationByType() - Successful.....");
            return ResponseEntity.ok(this.service.getCertification(type));
        }
        catch(Exception e)
        {
            log.error("Error in getCertificationByType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/translation/by")
    public ResponseEntity<TranslationResponse> getTranslationById(@RequestParam("id") final Integer id)
    {
        try
        {
            log.info("getTranslationByType() - Successful.....");
            return ResponseEntity.ok(this.service.getTranslate(id));
        }
        catch(Exception e)
        {
            log.error("Error in getTranslationByType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/image/by")
    public ResponseEntity<CollectionImageResponse> getImageCollection(@RequestParam("id") final Integer id)
    {
        try
        {
            log.info("getImageById() - Successful.....");
            return ResponseEntity.ok(this.service.getImageCollection(id));
        }
        catch(Exception e)
        {
            log.error("Error in getImageById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/collection/details/by")
    public ResponseEntity<CollectionResponse> getCollectionDetailsById(@RequestParam("id") final Integer id)
    {
        try
        {
            log.info("getDetailsById() - Successful.....");
            return ResponseEntity.ok(this.service.getCollectionDetail(id));
        }
        catch(Exception e)
        {
            log.error("Error in getDetailsById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/company/details/by")
    public ResponseEntity<CompanyResponse> getCompanyDetailsById(@RequestParam("id") final Integer id)
    {
        try
        {
            log.info("getCompanyDetailsById() - Successful.....");
            return ResponseEntity.ok(this.service.getCompanyDetail(id));
        }
        catch(Exception e)
        {
            log.error("Error in getCompanyDetailsById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/timezones")
    public ResponseEntity<ConfigurationTimezonesResponse[]> getConfigurationTimezones()
    {
        try
        {
            log.info("getConfigurationTimezones() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationTimezones());
        }
        catch(Exception e)
        {
            log.error("Error in getConfigurationTimezones: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/primary_translations")
    public ResponseEntity<String[]> getConfigurationPrimaryTranslations()
    {
        try
        {
            log.info("getConfigurationPrimaryTranslations() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationPrimaryTranslations());
        }
        catch(Exception e)
        {
            log.error("Error in getConfigurationPrimaryTranslations: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/languages")
    public ResponseEntity<ConfigurationLanguageResponse[]> getConfigurationLanguages()
    {
        try
        {
            log.info("getConfigurationLanguages() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationLanguages());
        }
        catch(Exception e)
        {
            log.error("Error in getConfigurationLanguages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/countries")
    public ResponseEntity<ConfigurationCountryResponse[]> getConfigurationCountries(
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getConfigurationCountries() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationCountries(language));
        }
        catch(Exception e)
        {
            log.error("Error in getConfigurationCountries: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/jobs")
    public ResponseEntity<ConfigurationJobsResponse[]> getConfigurationJobs()
    {
        try
        {
            log.info("getConfigurationJobs() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationJobs());
        }
        catch(Exception e)
        {
            log.error("Error in getConfigurationJobs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration")
    public ResponseEntity<ConfigurationDetailsResponse> getConfigurationDetails()
    {
        try
        {
            log.info("getConfigurationDetails() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationDetails());
        }
        catch(Exception e)
        {
            log.error("Error in getConfigurationDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/credits/details/{id}")
    public ResponseEntity<DetailsResponse> getCreditsDetails(@PathVariable final String id)
    {
        try
        {
            log.info("getCreditsDetails() - Successful.....");
            return ResponseEntity.ok(this.service.getCreditsDetails(id));
        }
        catch(Exception e)
        {
            log.error("Error in getCreditsDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/discover/movie")
    public ResponseEntity<SearchResponse<MediaResponse>> getDiscoverMovie()
    {
        try
        {
            log.info("getDiscoverMovie() - Successful.....");
            return ResponseEntity.ok(this.service.getDiscoverMovie());
        }
        catch(Exception e)
        {
            log.error("Error in getDiscoverMovie: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/discover/tv")
    public ResponseEntity<SearchResponse> getDiscoverTV()
    {
        try
        {
            log.info("getDiscoverTV() - Successful.....");
            return ResponseEntity.ok(this.service.getDiscoverTV());
        }
        catch(Exception e)
        {
            log.error("Error in getDiscoverTV: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/keyword/{id}")
    public ResponseEntity<KeywordsResponse> getKeywordDetails(@PathVariable final Integer id)
    {
        try
        {
            log.info("getKeywordDetails() - Successful.....");
            return ResponseEntity.ok(this.service.getKeywordDetails(id));
        }
        catch(Exception e)
        {
            log.error("Error in getKeywordDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/keyword/movie/{id}")
    public ResponseEntity<KeywordMovieResponse> getKeywordMovieDetails(@PathVariable final Integer id,
            @RequestParam(value = "page", defaultValue = "1") final Integer page,
            @RequestParam(value = "include_adult", defaultValue = "") final Boolean include_adult)
    {
        try
        {
            log.info("getKeywordMovieDetails() - Successful.....");
            return ResponseEntity.ok(this.service.getKeywordMovieDetails(id, page, include_adult));
        }
        catch(Exception e)
        {
            log.error("Error in getKeywordMovieDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/now_playing")
    public ResponseEntity<NowPlayingResponse> getMovieNowPlaying(@RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page,
            @RequestParam(value = "region", defaultValue = "") final String region)
    {
        try
        {
            log.info("getMovieNowPlaying() - Successful.....");
            return ResponseEntity.ok(this.service.getMovieNowPlaying(language, page, region));
        }
        catch(Exception e)
        {
            log.error("Error in getMovieNowPlaying: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/popular")
    public ResponseEntity<PopularResponse> getMoviePopular(@RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page,
            @RequestParam(value = "region", defaultValue = "") final String region)
    {
        try
        {
            log.info("getMoviePopular() - Successful.....");
            return ResponseEntity.ok(this.service.getMoviePopular(language, page, region));
        }
        catch(Exception e)
        {
            log.error("Error in getMoviePopular: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/top_rated")
    public ResponseEntity<TopRatedResponse> getTopRated(@RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page,
            @RequestParam(value = "region", defaultValue = "") final String region)
    {
        try
        {
            log.info("getTopRated() - Successful.....");
            return ResponseEntity.ok(this.service.getTopRated(language, page, region));
        }
        catch(Exception e)
        {
            log.error("Error in getTopRated: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/upcoming")
    public ResponseEntity<UpComingResponse> getUnComing(@RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page,
            @RequestParam(value = "region", defaultValue = "") final String region)
    {
        try
        {
            log.info("getUnComing() - Successful.....");
            return ResponseEntity.ok(this.service.getUnComing(language, page, region));
        }
        catch(Exception e)
        {
            log.error("Error in getUnComing: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/credits/{id}")
    public ResponseEntity<CreditResponse> getMovieCredit(@PathVariable final Integer id,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getMovieCredit() - Successful.....");
            return ResponseEntity.ok(this.service.getMovieCredit(id, language));
        }
        catch(Exception e)
        {
            log.error("Error in getMovieCredit: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/details/{id}")
    public ResponseEntity<MediaResponse> getMovieDetails(@PathVariable final Integer id,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getMovieDetails() - Successful.....");
            return ResponseEntity.ok(this.service.getMovieDetails(id, language));
        }
        catch(Exception e)
        {
            log.error("Error in getMovieDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/external_ids/{id}")
    public ResponseEntity<MediaExternalIdResponse> getMovieExternalIds(@PathVariable final Integer id)
    {
        try
        {
            log.info("getMovieExternalIds() - Successful.....");
            return ResponseEntity.ok(this.service.getMovieExternalIds(id));
        }
        catch(Exception e)
        {
            log.error("Error in getMovieExternalIds: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/images/{id}")
    public ResponseEntity<MediaImagesResponse> getMovieImages(@PathVariable final Integer id)
    {
        try
        {
            log.info("getMovieImages() - Successful.....");
            return ResponseEntity.ok(this.service.getMovieImages(id));
        }
        catch(Exception e)
        {
            log.error("Error in getMovieImages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/movie/keywords/{id}")
    public ResponseEntity<MediaKeywordResponse> getKeywordMovie(@PathVariable final Integer id)
    {
        try
        {
            log.info("getKeywordMovie() - Successful.....");
            return ResponseEntity.ok(this.service.getKeywordMovie(id));
        }
        catch(Exception e)
        {
            log.error("Error in getKeywordMovie: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/movie/recommendation/{id}")
    public ResponseEntity<SearchResponse<MediaResponse>> getMediaRecommendation(@PathVariable final Integer id,
            @RequestParam(value = "type", required = true) final MediaType type,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getMediaRecommendation() - Successful.....");
            return ResponseEntity.ok(this.service.getMediaRecommendation(id, type, language, page));
        }
        catch(Exception e)
        {
            log.error("Error in getMediaRecommendation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/movie/release/date/{id}")
    public ResponseEntity<MovieReleaseDateResponse> getMovieReleaseDate(@PathVariable final Integer id)
    {
        try
        {
            log.info("getMovieReleaseDate() - Successful.....");
            return ResponseEntity.ok(this.service.getMovieReleaseDate(id));
        }
        catch(Exception e)
        {
            log.error("Error in getMovieReleaseDate: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/movie/similar/{id}")
    public ResponseEntity<SearchResponse<MediaResponse>> getMediaSimilar(@PathVariable final Integer id,
            @RequestParam(value = "type", required = true) final MediaType type,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getMediaSimilar() - Successful.....");
            return ResponseEntity.ok(this.service.getMediaSimilar(id, type, language, page));
        }
        catch(Exception e)
        {
            log.error("Error in getMediaSimilar: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/media/video/{id}")
    public ResponseEntity<MediaVideoResponse> getMediaVideo(@PathVariable final Integer id,
            @RequestParam(value = "type", required = true) final MediaType type,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "include_video_language", required = false) final String include_video_language)
    {
        try
        {
            log.info("getMediaVideo() - Successful.....");
            return ResponseEntity.ok(this.service.getMediaVideo(id, type, language, include_video_language));
        }
        catch(Exception e)
        {
            log.error("Error in getMovieVideo: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/movie/watch/provider/{id}")
    public ResponseEntity<MediaWatchProvidersResponse> getMediaWatchProvider(@PathVariable final Integer id,
            @RequestParam(value = "type", required = true) final MediaType type)
    {
        try
        {
            log.info("getMediaWatchProvider() - Successful.....");
            return ResponseEntity.ok(this.service.getMediaWatchProvider(id, type));
        }
        catch(Exception e)
        {
            log.error("Error in getMovieWatchProvider: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/network/detail/{id}")
    public ResponseEntity<NetworkDetailResponse> getNetworkDetail(@PathVariable final Integer id)
    {
        try
        {
            log.info("getNetworkDetail() - Successful.....");
            return ResponseEntity.ok(this.service.getNetworkDetail(id));
        }
        catch(Exception e)
        {
            log.error("Error in getNetworkDetail: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/network/alternative/names/{id}")
    public ResponseEntity<NetworkAlternativeNameResponse> getNetworkAlternativeName(@PathVariable final Integer id)
    {
        try
        {
            log.info("getNetworkAlternativeName() - Successful.....");
            return ResponseEntity.ok(this.service.getNetworkAlternativeName(id));
        }
        catch(Exception e)
        {
            log.error("Error in getNetworkAlternativeName: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/network/images/{id}")
    public ResponseEntity<NetworkImageResponse> getNetworkImage(@PathVariable final Integer id)
    {
        try
        {
            log.info("getNetworkImage() - Successful.....");
            return ResponseEntity.ok(this.service.getNetworkImage(id));
        }
        catch(Exception e)
        {
            log.error("Error in getNetworkImage: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/people/popular")
    public ResponseEntity<PeoplePopularResponse> getPeoplePopular(@RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getPeoplePopular() - Successful.....");
            return ResponseEntity.ok(this.service.getPeoplePopular(language, page));
        }
        catch(Exception e)
        {
            log.error("Error in getPeoplePopular: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/people/detail/{id}")
    public ResponseEntity<PeopleResponse> getPeopleDetail(@PathVariable final Integer id,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getPeopleDetail() - Successful.....");
            return ResponseEntity.ok(this.service.getPeopleDetail(id, language));
        }
        catch(Exception e)
        {
            log.error("Error in getPeopleDetail: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/people/combined/credit/{id}")
    public ResponseEntity<CreditResponse> getPeopleCombinedCredit(@PathVariable final Integer id,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getPeopleCombinedCredit() - Successful.....");
            return ResponseEntity.ok(this.service.getPeopleCombinedCredit(id, language));
        }
        catch(Exception e)
        {
            log.error("Error in getPeopleCombinedCredit: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/people/external/ids/{id}")
    public ResponseEntity<PersonExternalIdsResponse> getPersonExternalIds(@PathVariable final Integer id)
    {
        try
        {
            log.info("getPersonExternalIds() - Successful.....");
            return ResponseEntity.ok(this.service.getPersonExternalIds(id));
        }
        catch(Exception e)
        {
            log.error("Error in getPersonExternalIds: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/people/images/{id}")
    public ResponseEntity<PeopleImagesResponse> getPeopleImages(@PathVariable final Integer id)
    {
        try
        {
            log.info("getPeopleImages() - Successful.....");
            return ResponseEntity.ok(this.service.getPeopleImages(id));
        }
        catch(Exception e)
        {
            log.error("Error in getPeopleImages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/people/movie/cast/{id}")
    public ResponseEntity<CreditResponse> getPeopleMovieCast(@PathVariable final Integer id,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getPeopleMovieCast() - Successful.....");
            return ResponseEntity.ok(this.service.getPeopleMovieCast(id, language));
        }
        catch(Exception e)
        {
            log.error("Error in getPeopleMovieCast: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/people/tv/cast/{id}")
    public ResponseEntity<CreditResponse> getPeopleTvCast(@PathVariable final Integer id,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getPeopleTvCast() - Successful.....");
            return ResponseEntity.ok(this.service.getPeopleTvCast(id, language));
        }
        catch(Exception e)
        {
            log.error("Error in getPeopleTvCast: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/search/collection")
    public ResponseEntity<SearchResponse<CollectionResponse>> getSearchCollectionResponse(@RequestParam("query") final String query,
            @RequestParam(value = "include_adult", defaultValue = "false") final Boolean include_adult,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page,
            @RequestParam(value = "region", required = false) final String region)
    {
        try
        {
            log.info("getSearchCollectionResponse() - Successful.....");
            return ResponseEntity.ok(this.service.getSearchCollectionResponse(query, include_adult, language, page, region));
        }
        catch(Exception e)
        {
            log.error("Error in getSearchCollectionResponse: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/search/company")
    public ResponseEntity<SearchResponse<CompanyResponse>> getSearchCompanyResponse(@RequestParam("query") final String query,
            @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getSearchCompanyResponse() - Successful.....");
            return ResponseEntity.ok(this.service.getSearchCompanyResponse(query, page));
        }
        catch(Exception e)
        {
            log.error("Error in getSearchCompanyResponse: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/search/keyword")
    public ResponseEntity<SearchResponse<KeywordsResponse>> getSearchKeywordsResponse(
            @RequestParam(value = "query", required = true) final String query, @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getSearchKeywordsResponse() - Successful.....");
            return ResponseEntity.ok(this.service.getSearchKeywordsResponse(query, page));
        }
        catch(Exception e)
        {
            log.error("Error in getSearchKeywordsResponse: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/search/media")
    public ResponseEntity<SearchResponse<MediaResponse>> getSearchMediaResponse(@RequestParam(value = "type", required = false) final MediaType type,
            @RequestParam(value = "query", required = true) final String query,
            @RequestParam(value = "include_adult", defaultValue = "false") final Boolean include_adult,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "primary_release_year", required = false) final String primary_release_year,
            @RequestParam(value = "page", defaultValue = "1") final Integer page, final String region,
            @RequestParam(value = "year", required = false) final Integer year)
    {
        try
        {
            log.info("getSearchMediaResponse() - Successful.....");
            return ResponseEntity.ok(
                    this.service.getSearchMediaResponse(type, query, include_adult, language, primary_release_year, page, region, year));
        }
        catch(Exception e)
        {
            log.error("Error in getSearchMediaResponse: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/search/people")
    public ResponseEntity<SearchResponse<PeopleResponse>> getSearchPeopleResponse(@RequestParam(value = "query", required = true) final String query,
            @RequestParam(value = "include_adult", defaultValue = "false") final Boolean include_adult,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getSearchPeopleResponse() - Successful.....");
            return ResponseEntity.ok(this.service.getSearchPeopleResponse(query, include_adult, language, page));
        }
        catch(Exception e)
        {
            log.error("Error in getSearchPeopleResponse: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/trending/media")
    public ResponseEntity<SearchResponse<MediaResponse>> getTrendingMediaResponse(
            @RequestParam(value = "type", required = false) final MediaType type,
            @RequestParam(value = "time_window", required = true) final String time_window,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTrendingMediaResponse() - Successful.....");
            return ResponseEntity.ok(this.service.getTrendingMediaResponse(type, time_window, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTrendingMediaResponse: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/trending/people")
    public ResponseEntity<SearchResponse<PeopleResponse>> getTrendingPeopleResponse(
            @RequestParam(value = "time_window", required = true) final String time_window,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTrendingPeopleResponse() - Successful.....");
            return ResponseEntity.ok(this.service.getTrendingPeopleResponse(time_window, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTrendingPeopleResponse: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/details/{id}")
    public ResponseEntity<MediaResponse> getTvDetails(@PathVariable final Integer id,
            @RequestParam(value = "append_to_response", required = false) final String append_to_response,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTvDetails() - Successful.....");
            return ResponseEntity.ok(this.service.getTvDetails(id, append_to_response, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/aggregate/credit/{id}")
    public ResponseEntity<CreditResponse> getTvAggregateCredits(@PathVariable final Integer id,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTvAggregateCredits() - Successful.....");
            return ResponseEntity.ok(this.service.getTvAggregateCredits(id, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvAggregateCredits: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/external/{id}")
    public ResponseEntity<MediaExternalIdResponse> getTvExternalIds(@PathVariable final Integer id)
    {
        try
        {
            log.info("getTvExternalIds() - Successful.....");
            return ResponseEntity.ok(this.service.getTvExternalIds(id));
        }
        catch(Exception e)
        {
            log.error("Error in getTvExternalIds: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/images/{id}")
    public ResponseEntity<MediaImagesResponse> getTvImages(@PathVariable final Integer id,
            @RequestParam(value = "include_image_language", required = false) final String include_image_language,
            @RequestParam(value = "language", required = false) final String language)
    {
        try
        {
            log.info("getTvImages() - Successful.....");
            return ResponseEntity.ok(this.service.getTvImages(id, include_image_language, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvImages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/keyword/{id}")
    public ResponseEntity<MediaKeywordResponse> getTvKeyword(@PathVariable final Integer id)
    {
        try
        {
            log.info("getTvKeyword() - Successful.....");
            return ResponseEntity.ok(this.service.getTvKeyword(id));
        }
        catch(Exception e)
        {
            log.error("Error in getTvKeyword: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/details/{series_id}/{series_number}")
    public ResponseEntity<TvSeasonDetailsResponse> getTvSeasonDetails(@PathVariable final Integer series_id,
            @PathVariable final Integer series_number, @RequestParam(value = "append_to_response", required = false) final String append_to_response,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTvSeasonDetails() - Successful.....");
            return ResponseEntity.ok(this.service.getTvSeasonDetails(series_id, series_number, append_to_response, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/credits/{series_id}/{series_number}")
    public ResponseEntity<CreditResponse> getTvSeasonCredit(@PathVariable final Integer series_id, @PathVariable final Integer series_number,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTvSeasonCredit() - Successful.....");
            return ResponseEntity.ok(this.service.getTvSeasonCredit(series_id, series_number, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonCredit: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/externalId/{series_id}/{series_number}")
    public ResponseEntity<MediaExternalIdResponse> getTvSeasonExternalId(@PathVariable final Integer series_id,
            @PathVariable final Integer series_number)
    {
        try
        {
            log.info("getTvSeasonExternalId() - Successful.....");
            return ResponseEntity.ok(this.service.getTvSeasonExternalId(series_id, series_number));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonExternalId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/watch/provider/{series_id}/{series_number}")
    public ResponseEntity<MediaWatchProvidersResponse> getTvSeasonMediaWatchProvider(@PathVariable final Integer series_id,
            @PathVariable final Integer series_number, @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTvSeasonMediaWatchProvider() - Successful.....");
            return ResponseEntity.ok(this.service.getTvSeasonMediaWatchProvider(series_id, series_number, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonMediaWatchProvider: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/video/{series_id}/{season_number}")
    public ResponseEntity<MediaVideoResponse> getTvSeasonMediaVideo(@PathVariable final Integer series_id, @PathVariable final Integer season_number,
            @RequestParam(value = "include_video_language", required = false) final String include_video_language,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTvSeasonMediaVideo() - Successful.....");
            return ResponseEntity.ok(this.service.getTvSeasonMediaVideo(series_id, season_number, include_video_language, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonMediaVideo: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/image/{series_id}/{season_number}")
    public ResponseEntity<MediaImagesResponse> getTvSeasonMediaImage(@PathVariable final Integer series_id, @PathVariable final Integer season_number,
            @RequestParam(value = "include_video_language", required = false) final String include_video_language,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getTvSeasonMediaImage() - Successful.....");
            return ResponseEntity.ok(this.service.getTvSeasonMediaImage(series_id, season_number, include_video_language, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonMediaImage: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/episode/external_ids/{series_id}/{season_number}/{episode_number}")
    public ResponseEntity<MediaExternalIdResponse> getTvSeasonEpisodeExternal(@PathVariable final Integer series_id,
            @PathVariable final Integer season_number, @PathVariable final Integer episode_number)
    {
        try
        {
            log.info("getTvSeasonEpisodeExternal() - Successful.....");
            return ResponseEntity.ok(this.service.getTvSeasonEpisodeExternal(series_id, season_number, episode_number));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonEpisodeExternal: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/episode/images/{series_id}/{season_number}/{episode_number}")
    public ResponseEntity<MediaImagesResponse> getTvSeasonEpisodeImage(@PathVariable final Integer series_id,
            @PathVariable final Integer season_number, @PathVariable final Integer episode_number,
            @RequestParam(name = "include_image_language", required = false) final String include_image_language,
            @RequestParam(name = "language", required = false) final String language)
    {
        try
        {
            log.info("getTvSeasonEpisodeImage() - Successful.....");
            return ResponseEntity.ok(
                    this.service.getTvSeasonEpisodeImage(series_id, season_number, episode_number, include_image_language, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonEpisodeImage: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/tv/season/episode/videos/{series_id}/{season_number}/{episode_number}")
    public ResponseEntity<MediaVideoResponse> getTvSeasonEpisodeVideo(@PathVariable final Integer series_id,
            @PathVariable final Integer season_number, @PathVariable final Integer episode_number,
            @RequestParam(name = "include_image_language", required = false) final String include_image_language,
            @RequestParam(name = "language", required = false) final String language)
    {
        try
        {
            log.info("getTvSeasonEpisodeVideo() - Successful.....");
            return ResponseEntity.ok(
                    this.service.getTvSeasonEpisodeVideo(series_id, season_number, episode_number, include_image_language, language));
        }
        catch(Exception e)
        {
            log.error("Error in getTvSeasonEpisodeVideo: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get/change/{type}")
    public ResponseEntity<ChangesResponse> getChangeResponse(@PathVariable final MediaType type,
            @RequestParam(name = "end_date", required = false) final Date end_date,
            @RequestParam(name = "page", defaultValue = "1") final Integer page,
            @RequestParam(name = "start_date", required = false) final Date start_date)
    {
        try
        {
            log.info("getChangeResponse() - Successful.....");
            int count = 0;
            ChangesResponse changesResponse = new ChangesResponse();
            do
            {
                count++;
                changesResponse = this.service.getChangeResponse(MediaType.MOVIE, null, page, null);
                for(ChangesResponse.Change change : changesResponse.getResults())
                {
                    this.mediaService.getMediaByTmdbIdAndMediaType(change.getId(), type);
                }
            }
            while(count == 1);
            return ResponseEntity.ok(this.service.getChangeResponse(type, end_date, page, start_date));
        }
        catch(Exception e)
        {
            log.error("Error in getChangeResponse: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
