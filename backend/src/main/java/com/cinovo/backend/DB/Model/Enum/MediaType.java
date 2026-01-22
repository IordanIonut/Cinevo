package com.cinovo.backend.DB.Model.Enum;

public enum MediaType
{
    TV_EPISODE, TV_SEASON, MOVIE, TV, PERSON,

    COLLECTION, DISCOVER, RECOMMENDATION, SIMILAR, POPULARITY, SEARCH, TRENDING, FREE_TO_WATCH;

    public static MediaType resolveMediaType(String raw)
    {
        if(raw == null || raw.isBlank())
        {
            return null;
        }

        try
        {
            MediaType type = MediaType.valueOf(raw.toUpperCase());
            return (type == MediaType.TV_SEASON) ? MediaType.TV : type;
        }
        catch(IllegalArgumentException ex)
        {
            return null;
        }
    }
}

