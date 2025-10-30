package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DiscoverTVResponse {
    private Integer page;
    private List<DiscoverTVResponse.Result> results;
    private Integer total_pages;
    private Integer total_results;

    @Data
    @Getter
    @Setter
    public static class Result {
        private Boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private Integer id;
        private List<String> origin_country;
        private String original_language;
        private String original_name;
        private String overview;
        private Double popularity;
        private String poster_path;
        private String first_air_date;
        private String name;
        private Double vote_average;
        private Integer vote_count;
    }
}
