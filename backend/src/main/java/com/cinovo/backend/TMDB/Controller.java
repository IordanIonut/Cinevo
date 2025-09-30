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
    public ResponseEntity<TimezonesResponse[]> getTimezones() {
        try {
            log.info("getTimezones() - Successful.....");
            return ResponseEntity.ok(this.service.getTimezones());
        } catch (Exception e) {
            log.error("Error in getTimezones: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/primary_translations")
    public ResponseEntity<String[]> getPrimaryTranslations() {
        try {
            log.info("getPrimaryTranslations() - Successful.....");
            return ResponseEntity.ok(this.service.getPrimaryTranslations());
        } catch (Exception e) {
            log.error("Error in getPrimaryTranslations: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/configuration/languages")
    public ResponseEntity<LanguageResponse[]> getLanguages() {
        try {
            log.info("getLanguages() - Successful.....");
            return ResponseEntity.ok(this.service.getLanguages());
        } catch (Exception e) {
            log.error("Error in getLanguages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
