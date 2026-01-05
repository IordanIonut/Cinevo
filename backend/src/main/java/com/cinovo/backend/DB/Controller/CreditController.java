package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Service.CreditService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/credit")
@JBossLog
public class CreditController
{
    @Autowired
    private CreditService creditService;

    @GetMapping("/get/by/media/{media_tmdb_id}/{type}")
    public ResponseEntity<List<Credit>> findByMediaTmdbId(@PathVariable("media_tmdb_id") final Integer media_tmdb_id,
            @PathVariable("type") final MediaType type)
    {
        try
        {
            log.info("findByMediaTmdbId() - Successful.....");
            return ResponseEntity.ok(this.creditService.findByMediaTmdbId(media_tmdb_id, type));
        }
        catch(Exception e)
        {
            log.error("Error in findByMediaTmdbId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/by/person/{person_tmdb_id}")
    public ResponseEntity<List<Credit>> findByPersonTmdbId(@PathVariable("person_tmdb_id") final Integer person_tmdb_id)
    {
        try
        {
            log.info("findByPersonTmdbId() - Successful.....");
            return ResponseEntity.ok(this.creditService.findByPersonTmdbId(person_tmdb_id));
        }
        catch(Exception e)
        {
            log.error("Error in findByPersonTmdbId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
