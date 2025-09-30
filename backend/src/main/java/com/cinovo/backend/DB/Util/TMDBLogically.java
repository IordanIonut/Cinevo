package com.cinovo.backend.DB.Util;

public interface TMDBLogically<T, R> {
    public R onConvertTMDB(T input) throws Exception;
}
