package com.cinovo.backend.Enum;

import lombok.Getter;

@Getter
public enum SiteType
{
    YOUTUBE("Youtube"), mVIMEO("Vimeo");

    private final String label;

    SiteType(String label)
    {
        this.label = label;
    }

    public static SiteType fromLabel(String label)
    {
        if(label == null || label.isBlank())
        {
            return null;
        }

        for(SiteType status : values())
        {
            if(status.label.equalsIgnoreCase(label))
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + label);
    }
    }
