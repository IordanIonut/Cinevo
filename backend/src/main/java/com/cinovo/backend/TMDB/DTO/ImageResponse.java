package com.cinovo.backend.TMDB.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ImageResponse {
    private Integer id;
    private List<BackDrop> backdrops;
    private List<Poster> posters;

    @Getter
    @Setter
    @Data
    public static class BackDrop {
        private Double aspect_ratio;
        private Long height;
        private Long width;
        private String iso_639_1;
        private String file_path;
        private Double vote_average;
        private Long vote_count;
    }

    @Getter
    @Setter
    @Data
    public static class Poster {
        private Double aspect_ratio;
        private Long height;
        private Long width;
        private String iso_639_1;
        private String file_path;
        private Double vote_average;
        private Long vote_count;
    }
}
