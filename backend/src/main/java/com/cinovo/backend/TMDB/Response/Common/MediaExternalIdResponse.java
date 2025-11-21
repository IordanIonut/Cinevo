package com.cinovo.backend.TMDB.Response.Common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MediaExternalIdResponse
{
    private Integer id;
    private String imdb_id;
    private String freebase_mid;
    private String freebase_id;
    private Integer tvdb_id;
    private String tvrage_id;
    private String wikidata_id;
    private String facebook_id;
    private String twitter_id;
    private String instagram_id;
}
