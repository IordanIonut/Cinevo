package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class SearchResponse<U>
{
    private Integer page;
    private List<U> results;
    private Integer total_pages;
    private Integer total_results;
}

