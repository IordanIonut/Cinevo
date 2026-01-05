package com.cinovo.backend.DB.Model.Enum;

public enum VideoType
{
    TRAILER("Trailer"), TEASER("Teaser"), BEHIND_THE_SCENES("Behind the Scenes"), FEATURETTE("Featurette"), CLIP("Clip"), BLOOPERS("Bloopers"),
    OPENING_CREDITS("Opening Credits"), RECAP("Recap");

    private final String label;

    VideoType(String label)
    {
        this.label = label;
    }

    public static VideoType fromLabel(String label)
    {
        if(label == null || label.isBlank())
        {
            return null;
        }

        for(VideoType type : values())
        {
            if(type.label.equalsIgnoreCase(label))
            {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + label);
    }
}
