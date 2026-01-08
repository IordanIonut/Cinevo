package com.cinovo.backend.DB.Util.Helper;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.Schedule.VariableStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobHelper
{
    public List<Integer> getTrendingList(MediaType type, TimeWindow timeWindow)
    {
        boolean isDay = "day".equalsIgnoreCase(timeWindow.name());

        return switch(type)
        {
            case MOVIE -> isDay ? VariableStore.getTrendingMovieDay() : VariableStore.getTrendingMovieWeek();
            case TV -> isDay ? VariableStore.getTrendingTvDay() : VariableStore.getTrendingTvWeek();
            case PERSON -> isDay ? VariableStore.getTrendingPersonDay() : VariableStore.getTrendingPersonWeek();
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    public void updateTrendingList(final MediaType type, final TimeWindow time_window, List<Integer> numbers)
    {
        List<Integer> targetList = getTrendingList(type, time_window);

        targetList.clear();
        targetList.addAll(numbers);
    }

}
