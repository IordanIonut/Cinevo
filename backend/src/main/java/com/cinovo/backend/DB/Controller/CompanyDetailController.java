package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.CollectionDetail;
import com.cinovo.backend.DB.Model.CompanyDetail;
import com.cinovo.backend.DB.Service.CollectionDetailService;
import com.cinovo.backend.DB.Service.CompanyDetailService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/company/detail")
@JBossLog
public class CompanyDetailController {
    @Autowired
    private CompanyDetailService companyDetailService;

    @GetMapping("/get/by")
    public ResponseEntity<CompanyDetail> getCompanyDetailById(@RequestParam("id") final Integer id) {
        try {
            log.info("getCompanyDetailById() - Successful.....");
            return ResponseEntity.ok(this.companyDetailService.findCompanyDetailById(id));
        } catch (Exception e) {
            log.error("Error in getCompanyDetailById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
