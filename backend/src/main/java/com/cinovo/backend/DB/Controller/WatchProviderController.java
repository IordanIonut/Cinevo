package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Translate;
import com.cinovo.backend.DB.Model.WatchProvider;
import com.cinovo.backend.DB.Service.WatchProviderService;
import lombok.Getter;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/watch/provider")
@JBossLog
public class WatchProviderController
{
    private final WatchProviderService watchProviderService;

    public WatchProviderController(WatchProviderService watchProviderService)
    {
        this.watchProviderService = watchProviderService;
    }

    @GetMapping("/get/by/{tmdb_id}/{media_type}")
    public ResponseEntity<List<WatchProvider>> findByMediaTmdbIdAndMediaType(@PathVariable("tmdb_id") final Integer tmdb_id,
            @PathVariable("media_type") final MediaType media_type) throws Exception
    {
        try
        {
            log.info("findByMediaTmdbIdAndMediaType() - Successful.....");
            return ResponseEntity.ok(this.watchProviderService.findByMediaTmdbIdAndMediaType(tmdb_id, media_type));
        }
        catch(Exception e)
        {
            log.error("Error in findByMediaTmdbIdAndMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
