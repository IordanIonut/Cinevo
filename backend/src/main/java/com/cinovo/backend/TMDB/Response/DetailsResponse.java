package com.cinovo.backend.TMDB.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DetailsResponse {
    private String credit_type;
    private String department;
    private String job;
    private Media media;
    private String media_type;
    private String id;
    private Person person;

    @Data
    @Getter
    @Setter
    public static class Media {
        private Boolean adult;
        private String backdrop_path;
        private Integer id;
        private String name;
        private String original_name;
        private String overview;
        private String poster_path;
        private String media_type;
        private String original_language;
        private List<Integer> genre_ids;
        private Double popularity;
        private String first_air_date;
        private Double vote_average;
        private Integer vote_count;
        private List<String> origin_country;
        private String character;
        private List<Episodes> episodes;
        private List<Seasons> seasons;

        @Data
        @Getter
        @Setter
        public static class Episodes {
            private Integer id;
            private String name;
            private String overview;
            private String media_type;
            private Double vote_average;
            private Integer vote_count;
            private String air_date;
            private Integer episode_number;
            private String episode_type;
            private String production_code;
            private Integer runtime;
            private Integer season_number;
            private Integer show_id;
            private String still_path;
        }

        @Data
        @Getter
        @Setter
        public static class Seasons {
            private Integer id;
            private String name;
            private String overview;
            private String poster_path;
            private String media_type;
            private Double vote_average;
            private String air_date;
            private Integer season_number;
            private Integer show_id;
            private Integer episode_count;
        }
    }

    @Data
    @Getter
    @Setter
    public static class Person {
        private Boolean adult;
        private Integer id;
        private String name;
        private String original_name;
        private String media_type;
        private Double popularity;
        private Integer gender;
        private String known_for_department;
        private String profile_path;
    }
}
