package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.PeopleResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PeoplePopularResponse
{
    private Integer page;
    private List<PeopleResponse> results;
    private Integer total_pages;
    private Integer total_results;
}
