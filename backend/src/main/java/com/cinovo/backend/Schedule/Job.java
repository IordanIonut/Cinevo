package com.cinovo.backend.Schedule;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.DB.Service.MediaService;
import com.cinovo.backend.TMDB.Service;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

@Component
@JBossLog
public class Job
{
    private final Semaphore semaphore = new Semaphore(10);
    private final Service service;
    private final MediaService mediaService;

    public Job(Service service, MediaService mediaService)
    {
        this.service = service;
        this.mediaService = mediaService;
    }

    //CONFIGURATION
    public static String configurationUrlImages;

    //TRENDING
    public static List<Integer> trendingMediaMovieDay = new CopyOnWriteArrayList<>();
    public static List<Integer> trendingMediaMovieWeek = new CopyOnWriteArrayList<>();
    public static List<Integer> trendingMediaTvDay = new CopyOnWriteArrayList<>();
    public static List<Integer> trendingMediaTvWeek = new CopyOnWriteArrayList<>();
    public static List<Integer> trendingPersonDay = new CopyOnWriteArrayList<>();
    public static List<Integer> trendingPersonWeek = new CopyOnWriteArrayList<>();

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void callConfigurationDetails() throws Exception
    {
        Job.configurationUrlImages = this.service.getConfigurationDetails().getImages().getSecure_base_url();
    }

    //    @Scheduled(fixedRate = 2 * 60 * 60 * 1000)
    @Scheduled(fixedRate = 150)
    public void callTrendingAll() throws Exception
    {
        //        if(!semaphore.tryAcquire())
        //        {
        //            return;
        //        }
        //        try
        //        {
        //            log.error("start another schedule job");
        //            mediaService.getMediaUsingTrending(MediaType.MOVIE, TimeWindow.DAY, "en-US");
        //        }
        //        finally
        //        {
        //            semaphore.release();
        //        }
    }
}
