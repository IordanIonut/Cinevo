package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Service.CreditService;
import com.cinovo.backend.Enum.MediaType;
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

    @GetMapping("/get/by/media/{media_id}/{type}")
    public ResponseEntity<List<Credit>> findCreditByMediaIdAndType(@PathVariable("media_id") final Integer media_id,
            @PathVariable("type") final MediaType type)
    {
        try
        {
            log.info("findCreditByMediaIdAndType() - Successful.....");
            return ResponseEntity.ok(this.creditService.findCreditByMediaIdAndType(media_id, type));
        }
        catch(Exception e)
        {
            log.error("Error in findCreditByMediaIdAndType: {}", e.getMessage(), e);
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
