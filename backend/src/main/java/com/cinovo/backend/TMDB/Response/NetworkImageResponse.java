package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.DB.Model.Network;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class NetworkImageResponse
{
    private Integer id;
    private List<Logo> logos;

    @Data
    @Getter
    @Setter
    public static class Logo
    {
        private Double aspect_ratio;
        private String file_path;
        private Integer height;
        private Integer width;
        private String id;
        private String file_type;
        private Double vote_average;
        private Integer vote_count;
    }
}
