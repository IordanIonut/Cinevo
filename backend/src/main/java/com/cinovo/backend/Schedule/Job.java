package com.cinovo.backend.Schedule;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.DB.Service.MediaService;
import com.cinovo.backend.DB.Service.PersonService;
import com.cinovo.backend.TMDB.Service;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
@JBossLog
public class Job
{
    private final Service service;
    private final MediaService mediaService;
    private final PersonService personService;

    public Job(Service service, MediaService mediaService, PersonService personService)
    {
        this.service = service;
        this.mediaService = mediaService;
        this.personService = personService;
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

    //FREE TO WATCH
    public static List<Integer> freeToWatchMovie = new CopyOnWriteArrayList<>();
    public static List<Integer> freeToWatchTV = new CopyOnWriteArrayList<>();

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void callConfigurationDetails() throws Exception
    {
        Job.configurationUrlImages = this.service.getConfigurationDetails().getImages().getSecure_base_url() + "w342";
    }

    @Scheduled(fixedRate = 2 * 60 * 60 * 1000)
    public void callTrendingAll() throws Exception
    {
        this.mediaService.getMediaUsingTrending(MediaType.MOVIE, TimeWindow.DAY, "en-US");
        this.mediaService.getMediaUsingTrending(MediaType.MOVIE, TimeWindow.WEEK, "en-US");

        this.mediaService.getMediaUsingTrending(MediaType.TV, TimeWindow.DAY, "en-US");
        this.mediaService.getMediaUsingTrending(MediaType.TV, TimeWindow.WEEK, "en-US");

        this.personService.getPersonUsingTrending(TimeWindow.DAY, "en-US");
        this.personService.getPersonUsingTrending(TimeWindow.WEEK, "en-US");
    }

    @Scheduled(fixedRate = 3 * 60 * 1000)
    public void callFreeToWatch()
    {
        this.mediaService.getFreeToWatchByMediaType(MediaType.MOVIE, true);
        this.mediaService.getFreeToWatchByMediaType(MediaType.TV, true);
    }

}
