package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Image;
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
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/get/by")
    public ResponseEntity<List<Image>> findByTmdbIdAndMediaType(@RequestParam("tmdb_id") final Integer tmdb_id, @RequestParam("media_type")  final MediaType media_type) {
        try {
            log.info("findByTmdbIdAndMediaType() - Successful.....");
            return ResponseEntity.ok(this.imageService.findByTmdbIdAndMediaType(tmdb_id, media_type));
        } catch (Exception e) {
            log.error("Error in findByTmdbIdAndMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
