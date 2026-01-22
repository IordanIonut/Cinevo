package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.View.MediaView;
import com.cinovo.backend.DB.Service.MediaService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/media")
@JBossLog
public class MediaController
{
    @Autowired
    private MediaService mediaService;

    @GetMapping("/get/recommendation/{id}")
    public ResponseEntity<List<Media>> findRecommendationByIdAndType(@PathVariable final Integer id,
            @RequestParam(value = "type", required = true) final MediaType type)
    {
        try
        {
            log.info("findRecommendationByIdAndType() - Successful.....");
            return ResponseEntity.ok(this.mediaService.findRecommendationByIdAndType(id, type));
        }
        catch(Exception e)
        {
            log.error("Error in findMediaByRecommendation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/similar/{id}")
    public ResponseEntity<List<Media>> findMediaBySimilar(@PathVariable final Integer id,
            @RequestParam(value = "type", required = true) final MediaType type)
    {
        try
        {
            log.info("findMediaBySimilar() - Successful.....");
            return ResponseEntity.ok(this.mediaService.findMediaBySimilar(id, type));
        }
        catch(Exception e)
        {
            log.error("Error in findMediaBySimilar: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/discovery/{type}")
    public ResponseEntity<List<Media>> findMediaByDiscovery(@PathVariable final MediaType type)
    {
        try
        {
            log.info("findMediaByDiscovery() - Successful.....");
            return ResponseEntity.ok(this.mediaService.findMediaByDiscovery(type));
        }
        catch(Exception e)
        {
            log.error("Error in findMediaByDiscovery: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/{media_tmdb_id}/{type}")
    public ResponseEntity<Media> getMediaByTmdbIdAndMediaType(@PathVariable final Integer media_tmdb_id, @PathVariable final MediaType type)
    {
        try
        {
            log.info("getMediaByTmdbIdAndMediaType() - Successful.....");
            return ResponseEntity.ok(this.mediaService.getMediaByTmdbIdAndMediaType(media_tmdb_id, type));
        }
        catch(Exception e)
        {
            log.error("Error in getMediaByTmdbIdAndMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/search")
    public ResponseEntity<List<Media>> getMediaUsingSearch(@RequestParam(value = "type", required = false) final MediaType type,
            @RequestParam(value = "query", required = true) final String query,
            @RequestParam(value = "include_adult", defaultValue = "false") final Boolean include_adult,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "primary_release_year", required = false) final String primary_release_year,
            @RequestParam(value = "page", defaultValue = "1") final Integer page, final String region,
            @RequestParam(value = "year", required = false) final Integer year)
    {
        try
        {
            log.info("getMediaUsingSearch() - Successful.....");
            return ResponseEntity.ok(
                    this.mediaService.getMediaUsingSearch(type, query, include_adult, language, primary_release_year, page, region, year));
        }
        catch(Exception e)
        {
            log.error("Error in getMediaUsingSearch: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/trending")
    public ResponseEntity<List<MediaView>> getMediaUsingTrending(@RequestParam(value = "type", required = false) final MediaType type,
            @RequestParam(value = "time_window", required = true) final TimeWindow time_window,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getMediaUsingTrending() - Successful.....");
            return ResponseEntity.ok(this.mediaService.getMediaUsingTrending(type, time_window, language));
        }
        catch(Exception e)
        {
            log.error("Error in getMediaUsingTrending: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/free_to_watch/{media_type}")
    public ResponseEntity<List<MediaView>> getFreeToWatchByMediaType(@PathVariable final MediaType media_type)
    {
        try
        {
            log.info("getFreeToWatchByMediaType() - Successful.....");
            return ResponseEntity.ok(this.mediaService.getFreeToWatchByMediaType(media_type, false));
        }
        catch(Exception e)
        {
            log.error("Error in getFreeToWatchByMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
