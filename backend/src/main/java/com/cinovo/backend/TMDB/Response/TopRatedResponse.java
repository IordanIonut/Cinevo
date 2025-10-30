package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.MovieResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class TopRatedResponse {
    private Integer page;
    private List<MovieResponse> results;
    private Integer total_pages;
    private Integer total_results;
}
