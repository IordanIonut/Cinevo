package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.MovieStatus;
import com.cinovo.backend.TMDB.Response.MovieDetailsResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "MOVIE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Movie extends BaseEntity
{
    @Column(name = "ADULT")
    private Boolean adult;

    @Column(name = "BACKDROP_PATH")
    private String backdrop_path;

    @Column(name = "BUDGET")
    private Integer budget;

    @Column(name = "HOMEPAGE")
    private String homepage;

    @Column(name = "ID")
    private Integer id;

    @Column(name = "IMDB_ID")
    private String imdb_id;

    @Column(name = "WIKIDATA_ID")
    private String wikidata_id;

    @Column(name = "FACEBOOK_ID")
    private String facebook_id;

    @Column(name = "INSTAGRAM_ID")
    private String instagram_id;

    @Column(name = "TWITTER_ID")
    private String twitter_id;

    @ElementCollection
    @CollectionTable(name = "MEDIA_ORIGIN_COUNTRIES", joinColumns = @JoinColumn(name = "MEDIA_ID"))
    @Column(name = "ORIGIN_COUNTRY")
    private List<String> origin_country;

    @Column(name = "ORIGINAL_LANGUAGE")
    private String original_language;

    @Column(name = "ORIGINAL_TITLE")
    private String original_title;

    @Column(name = "OVERVIEW", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "POPULARITY")
    private Double popularity;

    @Column(name = "POSTER_PATH")
    private String poster_path;

    @Column(name = "RELEASE_DATE")
    private LocalDate release_date;

    @Column(name = "REVENUE")
    private Integer revenue;

    @Column(name = "RUNTIME")
    private Integer runtime;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MovieStatus status;

    @Column(name = "TAGLINE")
    private String tagline;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "VIDEO")
    private Boolean video;

    @Column(name = "VOTE_AVERAGE")
    private Double vote_average;

    @Column(name = "VOTE_COUNT")
    private Integer vote_count;

    @ManyToMany
    @JoinTable(name = "MOVIE_GENRE", joinColumns = @JoinColumn(name = "MOVIE_ID"), inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
    private List<Genre> genres;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BELONG_TO_COLLECTION_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private BelongToCollection belong_to_collection;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "MOVIE_PRODUCTION_COMPANY", joinColumns = @JoinColumn(name = "MOVIE_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<ProductionCompany> production_companies;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "MOVIE_PRODUCTION_COUNTRY", joinColumns = @JoinColumn(name = "MOVIE_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<ProductionCountry> production_countries;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "MOVIE_SPOKEN_LANGUAGE", joinColumns = @JoinColumn(name = "MOVIE_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<SpokenLanguage> spoken_languages;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Image> images;


    public final static String TABLE_AS = "movie";
    public final static String TABLE_NAME = "MOVIE ";
    public final static String ID = TABLE_AS + ".ID";
    public final static String CINEVO_ID =  TABLE_AS + ".CINEVO_ID";

    @Data
    @Entity
    @Table(name = "BELONG_TO_COLLECTION")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BelongToCollection extends BaseEntity
    {
        @Column(name = "ID")
        private Integer id;

        @Column(name = "NAME")
        private String name;

        @Column(name = "POSTER_PATH")
        private String poster_path;

        @Column(name = "BACKDROP_PATH")
        private String backdrop_path;
    }

    @Data
    @Entity
    @Table(name = "PRODUCTION_COMPANY")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductionCompany extends BaseEntity
    {
        @Column(name = "ID")
        private Integer id;

        @Column(name = "NAME")
        private String name;

        @Column(name = "LOGO_PATH")
        private String logo_path;

        @Column(name = "ORIGIN_COUNTRY")
        private String origin_country;
    }

    @Data
    @Entity
    @Table(name = "PRODUCTION_COUNTRY")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductionCountry extends BaseEntity
    {
        @Column(name = "ISO_3166_1")
        private String iso_3166_1;

        @Column(name = "NAME")
        private String name;
    }

    @Data
    @Entity
    @Table(name = "SPOKEN_LANGUAGE")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpokenLanguage extends BaseEntity
    {
        @Column(name = "ENGLISH_NAME")
        private String english_name;

        @Column(name = "ISO_639_1")
        private String iso_639_1;

        @Column(name = "NAME")
        private String name;
    }
}
