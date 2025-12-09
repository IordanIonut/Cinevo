package com.cinovo.backend.Enum;

import lombok.Getter;

@Getter
public enum SiteType
{
    YOUTUBE("Youtube"), TRAILER("Trailer"), TEASER("Teaser"), BEHIND_THE_SCENES("Behind the Scenes"), FEATURETTE("Featurette"), CLIP("Clip"),
    VIMEO("Vimeo"), BLOOPERS("Bloopers"), OPENING_CREDITS("Opening Credits"), RECAP("Recap");

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
