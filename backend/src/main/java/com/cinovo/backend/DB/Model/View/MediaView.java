package com.cinovo.backend.DB.Model.View;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.SiteType;

public interface MediaView
{
    Integer getTmdb_id();
    String getPoster_path();
    MediaType getType();
    String getCinevo_id();
    String getTitle();
    String getRelease_date();
    Double getVote_average();
    Integer getRuntime();
    Integer getSeason_number();
    String getFirst_air_date();
    String getKey();
    SiteType getSite();
}
