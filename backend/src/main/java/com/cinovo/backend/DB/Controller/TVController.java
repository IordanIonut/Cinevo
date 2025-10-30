package com.cinovo.backend.DB.Controller;


import com.cinovo.backend.DB.Model.Movie;
import com.cinovo.backend.DB.Model.TV;
import com.cinovo.backend.DB.Service.MovieService;
import com.cinovo.backend.DB.Service.TVService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/tv")
@JBossLog
public class TVController {
    @Autowired
    private TVService tvService;

    @GetMapping("/get/discovery")
    public ResponseEntity<List<TV>> findTVForDiscovery() {
        try {
            log.info("findTVForDiscovery() - Successful.....");
            return ResponseEntity.ok(this.tvService.findTVForDiscovery());
        } catch (Exception e) {
            log.error("Error in findTVForDiscovery: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
