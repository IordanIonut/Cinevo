package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.DB.Model.Network;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Model.View.PersonView;
import com.cinovo.backend.DB.Service.PersonService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/person")
@JBossLog
public class PersonController
{
    @Autowired
    private PersonService personService;

    @GetMapping("/get/popularity")
    public ResponseEntity<List<Person>> getPeoplePopularity(@RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getPeoplePopularity() - Successful.....");
            return ResponseEntity.ok(this.personService.getPeoplePopularity(page));
        }
        catch(Exception e)
        {
            log.error("Error in getPeoplePopularity: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/{tmdb_id}")
    public ResponseEntity<Person> findByTmdbId(@PathVariable final Integer tmdb_id)
    {
        try
        {
            log.info("findByTmdbId() - Successful.....");
            return ResponseEntity.ok(this.personService.findByTmdbId(tmdb_id));
        }
        catch(Exception e)
        {
            log.error("Error in findByTmdbId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/search")
    public ResponseEntity<List<Person>> getPersonUsingSearch(@RequestParam(value = "query", required = true) final String query,
            @RequestParam(value = "include_adult", defaultValue = "false") final Boolean include_adult,
            @RequestParam(value = "language", defaultValue = "en-US") final String language,
            @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getPersonUsingSearch() - Successful.....");
            return ResponseEntity.ok(this.personService.getPersonUsingSearch(query, include_adult, language, page));
        }
        catch(Exception e)
        {
            log.error("Error in getPersonUsingSearch: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/trending")
    public ResponseEntity<List<PersonView>> getPersonUsingTrending(@RequestParam(value = "time_window", required = true) final TimeWindow time_window,
            @RequestParam(value = "language", defaultValue = "en-US") final String language)
    {
        try
        {
            log.info("getPersonUsingTrending() - Successful.....");
            return ResponseEntity.ok(this.personService.getPersonUsingTrending(time_window, language));
        }
        catch(Exception e)
        {
            log.error("Error in getPersonUsingTrending: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
