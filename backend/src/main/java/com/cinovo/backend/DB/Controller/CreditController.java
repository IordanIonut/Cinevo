package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Credit;
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

    @GetMapping("/get/by/movie_id/{movie_id}")
    public ResponseEntity<List<Credit>> findCreditByMovieId(@PathVariable("movie_id") final Integer movie_id)
    {
        try
        {
            log.info("findCreditByMovieId() - Successful.....");
            return ResponseEntity.ok(this.creditService.findCreditByMovieId(movie_id));
        }
        catch(Exception e)
        {
            log.error("Error in findCreditByMovieId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/by/person_id/{person_id}")
    public ResponseEntity<List<Credit>> findCreditByPersonId(@PathVariable("person_id") final Integer person_id)
    {
        try
        {
            log.info("findCreditByPersonId() - Successful.....");
            return ResponseEntity.ok(this.creditService.findCreditByPersonId(person_id));
        }
        catch(Exception e)
        {
            log.error("Error in findCreditByPersonId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
