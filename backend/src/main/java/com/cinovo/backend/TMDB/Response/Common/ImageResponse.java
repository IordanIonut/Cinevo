package com.cinovo.backend.TMDB.Response.Common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ImageResponse
{
    private Double aspect_ratio;
    private Integer height;
    private String iso_3166_1;
    private String iso_639_1;
    private String file_path;
    private Double vote_average;
    private Integer vote_count;
    private Integer width;
}
