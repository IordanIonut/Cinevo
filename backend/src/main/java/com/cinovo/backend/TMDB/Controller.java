package com.cinovo.backend.TMDB;

import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Service.GenreService;
import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.DTO.*;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/TMDB")
@JBossLog
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/get/genre/by")
    public ResponseEntity<GenresResponse> getGenreByType(@RequestParam("type") final Type type) {
        try {
            log.info("getGenreByType() - Successful.....");
            return ResponseEntity.ok(this.service.getGenres(type));
        } catch (Exception e) {
            log.error("Error in getGenreByType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/certification/by")
    public ResponseEntity<CertificationResponse> getCertificationByType(@RequestParam("type") final Type type) {
        try {
            log.info("getCertificationByType() - Successful.....");
            return ResponseEntity.ok(this.service.getCertification(type));
        } catch (Exception e) {
            log.error("Error in getCertificationByType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/translation/by")
    public ResponseEntity<TranslationResponse> getTranslationById(@RequestParam("id") final Integer id) {
        try {
            log.info("getTranslationByType() - Successful.....");
            return ResponseEntity.ok(this.service.getTranslate(id));
        } catch (Exception e) {
            log.error("Error in getTranslationByType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/image/by")
    public ResponseEntity<ImageResponse> getImageById(@RequestParam("id") final Integer id) {
        try {
            log.info("getImageById() - Successful.....");
            return ResponseEntity.ok(this.service.getImage(id));
        } catch (Exception e) {
            log.error("Error in getImageById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/collection/details/by")
    public ResponseEntity<CollectionDetailsResponse> getCollectionDetailsById(@RequestParam("id") final Integer id) {
        try {
            log.info("getDetailsById() - Successful.....");
            return ResponseEntity.ok(this.service.getCollectionDetail(id));
        } catch (Exception e) {
            log.error("Error in getDetailsById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/company/details/by")
    public ResponseEntity<CompanyDetailsResponse> getCompanyDetailsById(@RequestParam("id") final Integer id) {
        try {
            log.info("getCompanyDetailsById() - Successful.....");
            return ResponseEntity.ok(this.service.getCompanyDetail(id));
        } catch (Exception e) {
            log.error("Error in getCompanyDetailsById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/timezones")
    public ResponseEntity<ConfigurationTimezonesResponse[]> getConfigurationTimezones() {
        try {
            log.info("getConfigurationTimezones() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationTimezones());
        } catch (Exception e) {
            log.error("Error in getConfigurationTimezones: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/primary_translations")
    public ResponseEntity<String[]> getConfigurationPrimaryTranslations() {
        try {
            log.info("getConfigurationPrimaryTranslations() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationPrimaryTranslations());
        } catch (Exception e) {
            log.error("Error in getConfigurationPrimaryTranslations: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/languages")
    public ResponseEntity<ConfigurationLanguageResponse[]> getConfigurationLanguages() {
        try {
            log.info("getConfigurationLanguages() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationLanguages());
        } catch (Exception e) {
            log.error("Error in getConfigurationLanguages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/countries")
    public ResponseEntity<ConfigurationCountryResponse[]> getConfigurationCountries(@RequestParam(value = "language", defaultValue = "en-US") final String language) {
        try {
            log.info("getConfigurationCountries() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationCountries(language));
        } catch (Exception e) {
            log.error("Error in getConfigurationCountries: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/jobs")
    public ResponseEntity<ConfigurationJobsResponse[]> getConfigurationJobs() {
        try {
            log.info("getConfigurationJobs() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationJobs());
        } catch (Exception e) {
            log.error("Error in getConfigurationJobs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration")
    public ResponseEntity<ConfigurationDetailsResponse> getConfigurationDetails() {
        try {
            log.info("getConfigurationDetails() - Successful.....");
            return ResponseEntity.ok(this.service.getConfigurationDetails());
        } catch (Exception e) {
            log.error("Error in getConfigurationDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
