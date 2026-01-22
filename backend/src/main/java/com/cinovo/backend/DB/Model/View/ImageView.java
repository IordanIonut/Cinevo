package com.cinovo.backend.DB.Model.View;

import com.cinovo.backend.DB.Model.Enum.ImageType;

public interface ImageView
{
    String getFile_path();
    String getCinevo_id();
    ImageType getType();
    Double getVote_average();
}
