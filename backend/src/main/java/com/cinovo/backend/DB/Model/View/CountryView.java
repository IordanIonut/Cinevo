package com.cinovo.backend.DB.Model.View;

import com.cinovo.backend.DB.Model.Enum.MediaType;

public interface CountryView
{
    String getCode();
    String getName();
    MediaType getType();
    String getCinevo_id();
}
