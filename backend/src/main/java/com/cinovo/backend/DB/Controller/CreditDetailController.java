package com.cinovo.backend.DB.Controller;


import com.cinovo.backend.DB.Model.Country;
import com.cinovo.backend.DB.Model.CreditDetails;
import com.cinovo.backend.DB.Service.CreditDetailsService;
import com.cinovo.backend.Enum.Type;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/credit/detail")
@JBossLog
public class CreditDetailController {
    @Autowired
    private CreditDetailsService creditDetailsService;

    @GetMapping("/get/by/id/{id}")
    public ResponseEntity<CreditDetails> findCreditDetailsById(@PathVariable("id") final String id) {
        try {
            log.info("findCreditDetailsById() - Successful.....");
            return ResponseEntity.ok(this.creditDetailsService.findCreditDetailsById(id));
        } catch (Exception e) {
            log.error("Error in findCreditDetailsById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
