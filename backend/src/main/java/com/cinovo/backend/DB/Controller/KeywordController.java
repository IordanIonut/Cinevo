package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Keyword;
import com.cinovo.backend.DB.Model.View.KeywordView;
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
public class KeywordController
{
    @Autowired
    private KeywordService keywordService;

    @GetMapping("/get/{tmdb_id}")
    public ResponseEntity<Keyword> findByTmdbId(@PathVariable final Integer tmdb_id)
    {
        try
        {
            log.info("findByTmdbId() - Successful.....");
            return ResponseEntity.ok(this.keywordService.findByTmdbId(tmdb_id));
        }
        catch(Exception e)
        {
            log.error("Error in findByTmdbId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/search/by")
    public ResponseEntity<List<Keyword>> getKeywordUsingSearch(@RequestParam("query") final String query,
            @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getKeywordUsingSearch() - Successful.....");
            return ResponseEntity.ok(this.keywordService.getKeywordUsingSearch(query, page));
        }
        catch(Exception e)
        {
            log.error("Error in getKeywordUsingSearch: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/keyword-view/{name}")
    public ResponseEntity<List<KeywordView>> findKeywordViewByName(@PathVariable final String name){
        try
        {
            log.info("findKeywordViewByName() - Successful.....");
            return ResponseEntity.ok(this.keywordService.findKeywordViewByName(name));
        }
        catch(Exception e)
        {
            log.error("Error in findKeywordViewByName: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
