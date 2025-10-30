package com.cinovo.backend.TMDB;

import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.GenresResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/TMDB")
@JBossLog
public class Controller
{
    @Autowired
    private Service service;

    @GetMapping("/get/genre/by")
    public ResponseEntity<GenresResponse> getGenreByType(@RequestParam("type") final Type type)
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
    public ResponseEntity<CertificationResponse> getCertificationByType(@RequestParam("type") final Type type)
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
    public ResponseEntity<CollectionDetailsResponse> getCollectionDetailsById(@RequestParam("id") final Integer id)
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
    public ResponseEntity<CompanyDetailsResponse> getCompanyDetailsById(@RequestParam("id") final Integer id)
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
    public ResponseEntity<DiscoverMovieResponse> getDiscoverMovie()
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
    public ResponseEntity<DiscoverTVResponse> getDiscoverTV()
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
    public ResponseEntity<KeywordsDetailResponse> getKeywordDetails(@PathVariable final Integer id)
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
    public ResponseEntity<MovieDetailsResponse> getMovieDetails(@PathVariable final Integer id,
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
    public ResponseEntity<MovieExternalIdsResponse> getMovieExternalIds(@PathVariable final Integer id)
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
    public ResponseEntity<MovieImagesResponse> getMovieImages(@PathVariable final Integer id)
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
}
