package com.cinovo.backend.Schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@JBossLog
public class VariableStore
{
    //CONFIGURATION
    @Getter @Setter
    private static String configurationUrl;

    //TRENDING
    //----MOVIE
    @Getter
    private static final List<Integer> trendingMovieDay = new CopyOnWriteArrayList<>();
    @Getter
    private static final List<Integer> trendingMovieWeek = new CopyOnWriteArrayList<>();
    //----TV
    @Getter
    private static final List<Integer> trendingTvDay = new CopyOnWriteArrayList<>();
    @Getter
    private static final List<Integer> trendingTvWeek = new CopyOnWriteArrayList<>();
    //----PEOPLE
    @Getter
    private static final List<Integer> trendingPersonDay = new CopyOnWriteArrayList<>();
    @Getter
    private static final List<Integer> trendingPersonWeek = new CopyOnWriteArrayList<>();
}
