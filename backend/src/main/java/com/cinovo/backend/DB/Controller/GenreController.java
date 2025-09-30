package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Repository.GenreRepository;
import com.cinovo.backend.DB.Service.GenreService;
import com.cinovo.backend.Enum.Type;
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
    public ResponseEntity<List<Genre>> getGenreByType(@RequestParam("type") final Type type) {
        try {
            log.info("getGenreByType() - Successful.....");
            return ResponseEntity.ok(this.genreService.findGenresByType(type));
        } catch (Exception e) {
            log.error("Error in getGenreByType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
