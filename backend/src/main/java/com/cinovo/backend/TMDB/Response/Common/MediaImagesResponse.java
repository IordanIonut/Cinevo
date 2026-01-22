package com.cinovo.backend.TMDB.Response.Common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MediaImagesResponse
{
    private Integer id;
    private List<ImageResponse> backdrops;
    private List<ImageResponse> posters;
    private List<ImageResponse> logos;
    private List<ImageResponse> stills;
}
