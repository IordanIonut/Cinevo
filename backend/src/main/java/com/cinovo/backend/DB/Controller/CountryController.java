package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Country;
import com.cinovo.backend.DB.Model.Genre;
import com.cinovo.backend.DB.Service.CountryService;
import com.cinovo.backend.Enum.Type;
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
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping("/get/by/type/{type}")
    public ResponseEntity<List<Country>> getCountryByType(@PathVariable("type") final Type type) {
        try {
            log.info("findCountryByType() - Successful.....");
            return ResponseEntity.ok(this.countryService.findCountryByType(type));
        } catch (Exception e) {
            log.error("Error in findCountryByType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/by/type-code/{type}/{code}")
    public ResponseEntity<Country> getCountryByTypeAndCode(@PathVariable("type") final Type type, @PathVariable("code") final String code) {
        try {
            log.info("getCountryByTypeAndCode() - Successful.....");
            return ResponseEntity.ok(this.countryService.findCountryByTypeAndCode(type, code));
        } catch (Exception e) {
            log.error("Error in getCountryByTypeAndCode: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
