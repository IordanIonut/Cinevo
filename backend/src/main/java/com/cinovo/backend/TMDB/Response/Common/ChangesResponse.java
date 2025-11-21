package com.cinovo.backend.TMDB.Response.Common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ChangesResponse
{
    private List<Change> results;
    private Integer page;
    private Integer total_pages;
    private Integer total_results;

    @Data
    @Getter
    @Setter
    public static class Change
    {
        private Integer id;
        private Boolean adult;
    }
}
