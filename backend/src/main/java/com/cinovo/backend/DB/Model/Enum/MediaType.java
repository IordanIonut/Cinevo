package com.cinovo.backend.Enum;

public enum MediaType
{
    TV_EPISODE, TV_SEASON, MOVIE, TV, PERSON,

    COLLECTION, DISCOVER, RECOMMENDATION, SIMILAR, POPULARITY, SEARCH, TRENDING;

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

