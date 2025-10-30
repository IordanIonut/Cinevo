package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MovieExternalIdsResponse
{
    private Integer id;
    private String imdb_id;
    private String wikidata_id;
    private String facebook_id;
    private String twitter_id;
    private String instagram_id;
}
