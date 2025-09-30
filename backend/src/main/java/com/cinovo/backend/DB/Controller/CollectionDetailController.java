package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.CollectionDetail;
import com.cinovo.backend.DB.Service.CollectionDetailService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/collection/detail")
@JBossLog
public class CollectionDetailController {
    @Autowired
    private CollectionDetailService collectionDetailService;

    @GetMapping("/get/by")
    public ResponseEntity<CollectionDetail> getDetailById(@RequestParam("id") final Integer id) {
        try {
            log.info("getDetailById() - Successful.....");
            return ResponseEntity.ok(this.collectionDetailService.findDetailById(id));
        } catch (Exception e) {
            log.error("Error in getDetailById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
