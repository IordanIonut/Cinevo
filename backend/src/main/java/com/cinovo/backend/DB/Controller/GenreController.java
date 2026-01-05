package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Service.GenreService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/genre")
@JBossLog
public class GenreController {
    @Autowired
    private GenreService genreService;

    @GetMapping("/get/by")
    public ResponseEntity<List<Genre>> getGenreByMediaType(@RequestParam("media_type") final MediaType media_type) {
        try {
            log.info("getGenreByMediaType() - Successful.....");
            return ResponseEntity.ok(this.genreService.getGenreByMediaType(media_type));
        } catch (Exception e) {
            log.error("Error in getGenreByMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
