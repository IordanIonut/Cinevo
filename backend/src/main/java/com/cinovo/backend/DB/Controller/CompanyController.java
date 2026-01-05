package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Collection;
import com.cinovo.backend.DB.Model.Company;
import com.cinovo.backend.DB.Service.CompanyService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/company/detail")
@JBossLog
public class CompanyController
{
    @Autowired
    private CompanyService companyService;

    @GetMapping("/get/by")
    public ResponseEntity<Company> findCompanyByTmdbId(@RequestParam("tmdb_id") final Integer tmdb_id)
    {
        try
        {
            log.info("findCompanyByTmdbId() - Successful.....");
            return ResponseEntity.ok(this.companyService.findCompanyByTmdbId(tmdb_id));
        }
        catch(Exception e)
        {
            log.error("Error in findCompanyByTmdbId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/search/by")
    public ResponseEntity<List<Company>> getCompanyUsingSearch(@RequestParam("query") final String query,
            @RequestParam(value = "page", defaultValue = "1") final Integer page)
    {
        try
        {
            log.info("getCompanyUsingSearch() - Successful.....");
            return ResponseEntity.ok(this.companyService.getCompanyUsingSearch(query, page));
        }
        catch(Exception e)
        {
            log.error("Error in getCompanyUsingSearch: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
