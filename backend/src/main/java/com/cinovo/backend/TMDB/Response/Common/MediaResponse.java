package com.cinovo.backend.TMDB.Response.Common;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MediaResponse
{
    private Boolean adult;
    private String backdrop_path;
    private List<CreatedBy> created_by;
    private List<Integer> episode_run_time;
    private List<Integer> genre_ids;
    private List<GenresResponse.Genre> genres;
    private Integer gender;
    private String known_for_department;
    private String total_episode_count;
    private String profile_path;
    private Integer id;
    private String original_language;
    @JsonAlias({ "original_title", "original_name" })
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    private String release_date;
    @JsonAlias({ "name", "title" })
    private String title;
    private Boolean video;
    private Double vote_average;
    private Integer vote_count;
    private String media_type;
    private String first_air_date;
    private List<String> origin_country;
    private Integer budget;
    private String homepage;
    private String imdb_id;
    private Long revenue;
    private Integer runtime;
    private String status;
    private String tagline;
    private String character;
    private String credit_id;
    private Integer cast_id;
    private Integer order;
    private Integer episode_count;
    private String first_credit_air_date;
    private String department;
    private String job;
    private Boolean in_production;
    private String last_air_date;
    private Integer number_of_episodes;
    private Integer number_of_seasons;
    private String type;
    private List<String> languages;
    private List<Network> networks;
    private List<Season> seasons;
    private EpisodeToAir last_episode_to_air;
    private EpisodeToAir next_episode_to_air;
    private BelongsToCollection belongs_to_collection;
    private List<ProductionCompany> production_companies;
    private List<ProductionCountry> production_countries;
    private List<SpokenLanguage> spoken_languages;
    private List<Role> roles;
    private List<Job> jobs;
    private String person_id;
    private List<Episode> episodes;

    @Data
    @Getter
    @Setter
    public static class SpokenLanguage
    {
        private String english_name;
        private String iso_639_1;
        private String name;
    }

    @Data
    @Setter
    @Getter
    public static class ProductionCountry
    {
        private String iso_3166_1;
        private String name;
    }

    @Getter
    @Setter
    @Data
    public static class BelongsToCollection
    {
        private Integer id;
        private String name;
        private String poster_path;
        private String backdrop_path;
    }

    @Getter
    @Setter
    @Data
    public static class ProductionCompany
    {
        private Integer id;
        private String name;
        private String logo_path;
        private String origin_country;
    }

    @Getter
    @Setter
    @Data
    public static class CreatedBy
    {
        private Integer id;
        private String credit_id;
        private String name;
        private String original_name;
        private Integer gender;
        private String profile_path;
    }

    @Getter
    @Setter
    @Data
    public static class EpisodeToAir
    {
        private Integer id;
        private String name;
        private String overview;
        private Integer vote_average;
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
    public static class Network
    {
        private Integer id;
        private String logo_path;
        private String name;
        private String origin_country;
    }

    @Data
    @Getter
    @Setter
    public static class Season
    {
        private Integer episode_count;
        private String air_date;
        private Integer id;
        private String name;
        private String overview;
        private String poster_path;
        private Integer season_number;
        private Integer vote_average;
        private String media_type;
        private Integer show_id;
    }

    @Data
    @Getter
    @Setter
    public static class Episode
    {
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
    public static class Role
    {
        private String credit_id;
        private String character;
        private Integer episode_count;
    }

    @Data
    @Getter
    @Setter
    public static class Job
    {
        private String credit_id;
        private String job;
        private Integer episode_count;
    }
}