package com.cinovo.backend.TMDB.Response;

import com.cinovo.backend.TMDB.Response.Common.GenresResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Data
@Setter
public class MovieDetailsResponse
{
    private Boolean adult;
    private String backdrop_path;
    private BelongsToCollection belongs_to_collection;
    private Integer budget;
    private List<GenresResponse.Genre> genres;
    private String homepage;
    private Integer id;
    private String imdb_id;
    private List<String> origin_country;
    private String original_language;
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    private List<ProductionCompany> production_companies;
    private List<ProductionCountry> production_countries;
    private String release_date;
    private Integer revenue;
    private Integer runtime;
    private List<SpokenLanguage> spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private Boolean video;
    private Double vote_average;
    private Integer vote_count;

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
}
