package com.cinovo.backend.DB.Model.Enum;

import lombok.Getter;

@Getter
public enum MediaStatus
{
    RELEASED("RELEASED"), IN_PRODUCTION("IN PRODUCTION"), PLANNED("PLANNED"), RETURNING_SERIES("Returning Series"), ENDED("Ended"),
    CANCELED("Canceled"), POST_PRODUCTION("Post Production"), RUMORED("Rumored"), TEASER("Teaser");

    private final String label;

    MediaStatus(String label)
    {
        this.label = label;
    }

    public static MediaStatus fromLabel(String label)
    {
        if(label == null || label.isBlank())
        {
            return null;
        }

        for(MediaStatus status : values())
        {
            if(status.label.equalsIgnoreCase(label))
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown media status: " + label);
    }
}
