package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.EpisodeType;
import com.cinovo.backend.Enum.MediaStatus;
import com.cinovo.backend.Enum.MediaType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "MEDIA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = { "videos", "watch_providers" })
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Media extends BaseEntity
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

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private MediaType type;

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

    @ElementCollection
    @CollectionTable(name = "MEDIA_LANGUAGE", joinColumns = @JoinColumn(name = "MEDIA_ID"))
    @Column(name = "LANGUAGE")
    private List<String> languages;

    @Column(name = "ORIGINAL_LANGUAGE")
    private String original_language;

    @Column(name = "ORIGINAL_TITLE")
    private String original_title;

    @Lob
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
    private MediaStatus status;

    @Lob
    @Column(name = "TAGLINE", columnDefinition = "TEXT")
    private String tagline;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "VIDEO")
    private Boolean video;

    @Column(name = "VOTE_AVERAGE")
    private Double vote_average;

    @Column(name = "VOTE_COUNT")
    private Integer vote_count;

    @Column(name = "FIRST_AIR_DATE")
    private LocalDate first_air_date;

    @ManyToMany
    @JoinTable(name = "MEDIA_GENRE", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
    private List<Genre> genres;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BELONG_TO_COLLECTION_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private BelongToCollection belong_to_collection;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "MEDIA_PRODUCTION_COMPANY", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<ProductionCompany> production_companies;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "MEDIA_PRODUCTION_COUNTRY", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<ProductionCountry> production_countries;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "MEDIA_SPOKEN_LANGUAGE", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<SpokenLanguage> spoken_languages;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Image> images;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "MEDIA_KEYWORD", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<Keyword> keywords;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Video> videos;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<WatchProvider> watch_providers;

    @ManyToOne
    @JoinColumn(name = "DETAIL_CINEVO_ID", referencedColumnName = "CINEVO_ID")
    @JsonBackReference
    private Collection detail;

    @OneToMany(mappedBy = "medias", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Person> created_by;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "MEDIA_NETWORK", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<Network> networks;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "MEDIA_SEASON", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "CINEVO_ID"))
    private List<Season> seasons;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LAST_EPISODE_TO_AIR_CINEVO_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private LastEpisodeToAir last_episode_to_air;

    //EpisodeRunTime need tp add

    public final static String TABLE_AS = "movie";
    public final static String TABLE_NAME = "MEDIA ";
    public final static String ID = TABLE_AS + ".ID";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";

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

    @Data
    @Entity
    @Table(name = "NETWORK")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Network extends BaseEntity
    {
        @Column(name = "ID")
        private Integer id;

        @Column(name = "LOGO_PATH")
        private String logo_path;

        @Column(name = "NAME")
        private String name;

        @Column(name = "ORIGIN_COUNTRY")
        private String origin_country;
    }

    @Data
    @Entity
    @Table(name = "SEASON")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Season extends BaseEntity
    {
        @Column(name = "ID")
        private Integer id;

        @Column(name = "AIR_DATE")
        private LocalDate air_date;

        @Column(name = "EPISODE_COUNT")
        private Integer episode_count;

        @Column(name = "NAME")
        private String name;

        @Lob
        @Column(name = "OVERVIEW", columnDefinition = "TEXT")
        private String overview;

        @Column(name = "POSTER_PATH")
        private String poster_path;

        @Column(name = "SEASON_NUMBER")
        private Integer season_number;

        @Column(name = "VOTE_AVERAGE")
        private Integer vote_average;
    }

    @Data
    @Entity
    @Table(name = "LAST_EPISODE_TO_AIR")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LastEpisodeToAir extends BaseEntity
    {
        @Column(name = "ID")
        private Integer id;

        @Column(name = "NAME")
        private String name;

        @Lob
        @Column(name = "OVERVIEW", columnDefinition = "TEXT")
        private String overview;

        @Column(name = "VOTE_AVERAGE")
        private Integer vote_average;

        @Column(name = "VOTE_COUNT")
        private Integer vote_count;

        @Column(name = "AIR_DATE")
        private LocalDate air_date;

        @Column(name = "EPISODE_NUMBER")
        private Integer episode_number;

        @Column(name = "EPISODE_TYPE")
        @Enumerated(EnumType.STRING)
        private EpisodeType episode_type;

        @Column(name = "PRODUCTION_CODE")
        private String production_code;

        @Column(name = "RUNTIME")
        private Integer runtime;

        @Column(name = "SEASON_NUMBER")
        private Integer season_number;

        @Column(name = "STILL_PAth")
        private String still_path;

        @ManyToOne
        @JoinColumn(name = "MEDIA_ID")
        @JsonIgnoreProperties("hibernateLazyInitializer")
        @JsonBackReference
        private Media media;
    }
}
