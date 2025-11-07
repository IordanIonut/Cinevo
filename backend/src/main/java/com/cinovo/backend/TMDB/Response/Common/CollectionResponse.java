package com.cinovo.backend.TMDB.Response.Common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CollectionResponse
{
    private Integer id;
    private String name;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private String original_language;
    private String original_name;
    private Boolean adult;
    private List<MediaResponse> parts;
}
