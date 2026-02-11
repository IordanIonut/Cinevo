package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Model.View.SpokenLanguageView;
import com.cinovo.backend.DB.Service.PersonService;
import com.cinovo.backend.DB.Service.SpokenLanguageService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/spoken-language")
@JBossLog
public class SpokenLanguageController
{
    @Autowired
    private SpokenLanguageService spokenLanguageService;

    @GetMapping("/get/spoken-language-view/{media_type}")
    public ResponseEntity<List<SpokenLanguageView>> getSpokenLanguageViewByMediaType(@PathVariable final MediaType media_type)
    {
        try
        {
            log.info("getSpokenLanguageViewByMediaType() - Successful.....");
            return ResponseEntity.ok(this.spokenLanguageService.getSpokenLanguageViewByMediaType(media_type));
        }
        catch(Exception e)
        {
            log.error("Error in getSpokenLanguageViewByMediaType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
