package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Collection;
import com.cinovo.backend.DB.Service.CollectionService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/collection/detail")
@JBossLog
public class CollectionController
{
    @Autowired
    private CollectionService collectionService;

    @GetMapping("/get/by")
    public ResponseEntity<Collection> findByTmdbId(@RequestParam("tmdb_id") final Integer tmdb_id)
    {
        try
        {
            log.info("findByTmdbId() - Successful.....");
            return ResponseEntity.ok(this.collectionService.findByTmdbId(tmdb_id));
        }
        catch(Exception e)
        {
            log.error("Error in findByTmdbId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/search/by")
    public ResponseEntity<List<Collection>> getCollectionUsingSearch(@RequestParam("query") final String query,
            @RequestParam(value = "include_adult", defaultValue = "false") final Boolean include_adult,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page,
            @RequestParam(value = "region", required = false) final String region)
    {
        try
        {
            log.info("getCollectionUsingSearch() - Successful.....");
            return ResponseEntity.ok(this.collectionService.getCollectionUsingSearch(query,include_adult, language, page, region));
        }
        catch(Exception e)
        {
            log.error("Error in getCollectionUsingSearch: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
