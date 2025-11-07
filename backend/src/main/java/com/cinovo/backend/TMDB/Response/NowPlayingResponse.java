package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.DatesResponse;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class NowPlayingResponse {
    private DatesResponse dates;
    private Integer page;
    private List<MediaResponse> results;
    private Integer total_pages;
    private Integer total_results;
}
