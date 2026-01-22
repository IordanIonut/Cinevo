package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.View.ImageView;
import com.cinovo.backend.DB.Service.ImageService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/image")
@JBossLog
public class ImageController
{
    @Autowired
    private ImageService imageService;

    @GetMapping("/get/by")
    public ResponseEntity<List<Image>> findByTmdbIdAndMediaType(@RequestParam("tmdb_id") final Integer tmdb_id,
            @RequestParam("media_type") final MediaType media_type)
    {
        try
        {
            log.info("findByTmdbIdAndMediaType() - Successful.....");
            return ResponseEntity.ok(this.imageService.findByTmdbIdAndMediaType(tmdb_id, media_type));
        }
        catch(Exception e)
        {
            log.error("Error in findByTmdbIdAndMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/by/image-view/{media_type}/{media_cinevo_id}")
    public ResponseEntity<List<ImageView>> findImageViewByMediaTypeAndCinevoId(@PathVariable MediaType media_type,
            @PathVariable String media_cinevo_id)
    {
        try
        {
            log.info("findImageViewByMediaTypeAndCinevoId() - Successful.....");
            return ResponseEntity.ok(this.imageService.findImageViewByMediaTypeAndCinevoId(media_type, media_cinevo_id));
        }
        catch(Exception e)
        {
            log.error("Error in findImageViewByMediaTypeAndCinevoId(): {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
