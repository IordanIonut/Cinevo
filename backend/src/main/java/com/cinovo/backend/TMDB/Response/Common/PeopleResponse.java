package com.cinovo.backend.TMDB.Response.Common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PeopleResponse
{
    private Integer id;
    private Boolean adult;
    private Integer gender;
    private String known_for_department;
    private String original_name;
    private String name;
    private String profile_path;
    private Double popularity;
    private List<String> also_known_as;
    private String biography;
    private String birthday;
    private String deathday;
    private String homepage;
    private String imdb_id;
    private String place_of_birth;
    private String media_type;
    private List<MediaResponse> known_for;
}
