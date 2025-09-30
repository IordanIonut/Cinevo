package com.cinovo.backend.TMDB.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
public class CollectionDetailsResponse {
    private Integer id;
    private String name;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private List<Part> parts;

    @Data
    @Getter
    @Setter
    public static class Part {
        private Boolean adult;
        private String backdrop_path;
        private Integer id;
        private String title;
        private String original_title;
        private String overview;
        private String poster_path;
        private String media_type;
        private String original_language;
        private List<Integer> genre_ids;
        private Double popularity;
        private String release_date;
        private Boolean video;
        private Double vote_average;
        private Integer vote_count;
    }
}
