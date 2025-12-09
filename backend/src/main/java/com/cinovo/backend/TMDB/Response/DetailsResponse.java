package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.cinovo.backend.TMDB.Response.Common.PeopleResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DetailsResponse {
    private String credit_type;
    private String department;
    private String job;
    private MediaResponse media;
    private String media_type;
    private String id;
    private PeopleResponse person;
}
