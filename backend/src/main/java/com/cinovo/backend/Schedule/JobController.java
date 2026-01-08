package com.cinovo.backend.Schedule;

import lombok.extern.jbosslog.JBossLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/schedule")
@JBossLog
public class JobController
{
    private final Job job;

    public JobController(Job job)
    {
        this.job = job;
    }

    @GetMapping("/job/get")
    public ResponseEntity<HashMap<String, Object>> findVariableValue()
    {
        try
        {
            log.info("findVariableValue() - Successful.....");
            HashMap<String, Object> map = new HashMap<>();
            //configuration
            map.put("configurationUrlImages", Job.configurationUrlImages);

            //trending
            map.put("trendingMediaMovieDay", Job.trendingMediaMovieDay);
            map.put("trendingMediaMovieWeek", Job.trendingMediaMovieWeek);
            map.put("trendingMediaTvDay", Job.trendingMediaTvDay);
            map.put("trendingMediaTvWeek", Job.trendingMediaTvWeek);
            map.put("trendingPersonDay", Job.trendingPersonDay);
            map.put("trendingPersonWeek", Job.trendingPersonWeek);
            return ResponseEntity.ok(map);
        }
        catch(Exception e)
        {
            log.error("Error in findVariableValue: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
