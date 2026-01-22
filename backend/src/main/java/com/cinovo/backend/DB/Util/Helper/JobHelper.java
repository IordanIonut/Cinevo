package com.cinovo.backend.DB.Util.Helper;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.Schedule.Job;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@JBossLog
public class JobHelper
{
    public List<Integer> getJobList(final MediaType type, final TimeWindow timeWindow, final MediaType second_media_type)
    {
        boolean isDay = timeWindow == TimeWindow.DAY;
        return switch(type)
        {
            case MOVIE -> isDay ? Job.trendingMediaMovieDay : Job.trendingMediaMovieWeek;
            case TV -> isDay ? Job.trendingMediaTvDay : Job.trendingMediaTvWeek;
            case PERSON -> isDay ? Job.trendingPersonDay : Job.trendingPersonWeek;
            case FREE_TO_WATCH -> second_media_type == MediaType.MOVIE ? Job.freeToWatchMovie : Job.freeToWatchTV;
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    public void updateJobList(final MediaType type, final TimeWindow time_window, List<Integer> numbers, MediaType second_media_type)
    {
        List<Integer> targetList = getJobList(type, time_window, second_media_type);
        targetList.clear();
        targetList.addAll(numbers);
    }

}
