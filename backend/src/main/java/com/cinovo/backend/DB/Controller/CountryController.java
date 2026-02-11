package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Country;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.View.CountryView;
import com.cinovo.backend.DB.Service.CountryService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/country")
@JBossLog
public class CountryController
{
    @Autowired
    private CountryService countryService;

    @GetMapping("/get/by/media-type/{type}")
    public ResponseEntity<List<Country>> findCountryByMediaType(@PathVariable final MediaType type)
    {
        try
        {
            log.info("findCountryByMediaType() - Successful.....");
            return ResponseEntity.ok(this.countryService.findCountryByMediaType(type));
        }
        catch(Exception e)
        {
            log.error("Error in findCountryByMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/country-view/by/media-type/{media_type}")
    public ResponseEntity<List<CountryView>> findCountryViewByMediaType(@PathVariable final MediaType media_type) {
        try
        {
            log.info("findCountryViewByMediaType() - Successful.....");
            return ResponseEntity.ok(this.countryService.findCountryViewByMediaType(media_type));
        }
        catch(Exception e)
        {
            log.error("Error in findCountryViewByMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/get/by/media-type/{type}/code/{code}")
    public ResponseEntity<Country> findCountryByMediaTypeAndCode(@PathVariable final MediaType type, @PathVariable final String code)
    {
        try
        {
            log.info("findCountryByMediaTypeAndCode() - Successful.....");
            return ResponseEntity.ok(this.countryService.findCountryByMediaTypeAndCode(type, code));
        }
        catch(Exception e)
        {
            log.error("Error in findCountryByMediaTypeAndCode: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
