package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Translate;
import com.cinovo.backend.DB.Service.TranslateService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/translate")
@JBossLog
public class TranslateController {
    @Autowired
    private TranslateService translateService;

    @GetMapping("/get/by")
    public ResponseEntity<List<Translate>> getTranslateById(@RequestParam("id") final Integer id) {
        try {
            log.info("getTranslateById() - Successful.....");
            return ResponseEntity.ok(this.translateService.findAllTranslateById(id));
        } catch (Exception e) {
            log.error("Error in getTranslateById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
