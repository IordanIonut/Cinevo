package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PersonExternalIdsResponse
{
    private Integer id;
    private String freebase_mid;
    private String freebase_id;
    private String imdb_id;
    private String tvrage_id;
    private String wikidata_id;
    private String facebook_id;
    private String twitter_id;
    private String youtube_id;
    private String instagram_id;
    private String tiktok_id;
}
