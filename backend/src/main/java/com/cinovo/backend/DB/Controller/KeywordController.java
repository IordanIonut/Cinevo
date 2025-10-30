package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Keyword;
import com.cinovo.backend.DB.Model.Movie;
import com.cinovo.backend.DB.Service.KeywordService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/keyword")
@JBossLog
public class KeywordController {
    @Autowired
    private KeywordService keywordService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Keyword> findKeywordById(@PathVariable final Integer id) {
        try {
            log.info("findKeywordById() - Successful.....");
            return ResponseEntity.ok(this.keywordService.findKeywordById(id));
        } catch (Exception e) {
            log.error("Error in findKeywordById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
