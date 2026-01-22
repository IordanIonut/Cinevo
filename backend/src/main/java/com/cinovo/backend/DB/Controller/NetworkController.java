package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Model.Network;
import com.cinovo.backend.DB.Service.NetworkService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/network")
@JBossLog
public class NetworkController
{
    @Autowired
    private NetworkService networkService;

    @GetMapping("/get/{tmdb_id}")
    public ResponseEntity<Network> getNetworkByTmdbId(@PathVariable final Integer tmdb_id)
    {
        try
        {
            log.info("getNetworkByTmdbId() - Successful.....");
            return ResponseEntity.ok(this.networkService.getNetworkByTmdbId(tmdb_id));
        }
        catch(Exception e)
        {
            log.error("Error in getNetworkByTmdbId: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
