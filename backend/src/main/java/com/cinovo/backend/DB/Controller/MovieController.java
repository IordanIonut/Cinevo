package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.CreditDetails;
import com.cinovo.backend.DB.Model.Movie;
import com.cinovo.backend.DB.Service.MovieService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/movie")
@JBossLog
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/get/discovery")
    public ResponseEntity<List<Movie>> findMovieForDiscovery() {
        try {
            log.info("findMovieForDiscovery() - Successful.....");
            return ResponseEntity.ok(this.movieService.findMovieForDiscovery());
        } catch (Exception e) {
            log.error("Error in findMovieForDiscovery: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable final  Integer id) {
        try {
            log.info("getMovieById() - Successful.....");
            return ResponseEntity.ok(this.movieService.getMovieById(id));
        } catch (Exception e) {
            log.error("Error in getMovieById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }}
