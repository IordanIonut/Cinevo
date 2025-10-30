package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Service.GenreService;
import com.cinovo.backend.DB.Service.ImageService;
import com.cinovo.backend.Enum.Type;
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
    public ResponseEntity<List<Image>> getImageById(@RequestParam("id") final Integer id, @RequestParam("type")  final Type type) {
        try {
            log.info("getImageById() - Successful.....");
            return ResponseEntity.ok(this.imageService.findImageById(id, type));
        } catch (Exception e) {
            log.error("Error in getImageById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
