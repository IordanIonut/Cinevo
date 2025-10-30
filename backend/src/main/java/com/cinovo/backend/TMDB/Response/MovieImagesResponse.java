package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.ImageResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MovieImagesResponse
{
    private List<ImageResponse> backdrops;
    private List<ImageResponse> posters;
    private Integer id;
    private List<ImageResponse> logos;
}
