package com.cinovo.backend.DB.Util;

import com.cinovo.backend.DB.Model.Video;

import java.util.List;

public interface TMDBLogically<T, R> {
    public R onConvertTMDB(T input) throws Exception;
}
