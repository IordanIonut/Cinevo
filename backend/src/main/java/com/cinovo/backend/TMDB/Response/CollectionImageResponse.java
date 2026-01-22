package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.ImageResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CollectionImageResponse {
    private Integer id;
    private List<ImageResponse> backdrops;
    private List<ImageResponse> posters;
}
