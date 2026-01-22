package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.EpisodeType;
import com.cinovo.backend.DB.Model.Enum.MediaStatus;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Schedule.Job;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "MEDIA", uniqueConstraints = { @UniqueConstraint(columnNames = { "TMDB_ID", "TYPE" }) })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Media extends BaseEntity
{
    @Column(name = "ADULT")
    private Boolean adult;

    @Column(name = "BACKDROP_PATH")
    private String backdrop_path;

    @Column(name = "BUDGET")
    private Integer budget;

    @Column(name = "TMDB_ID", nullable = false)
    private Integer tmdb_id;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Column(name = "HOMEPAGE")
    private String homepage;

    @ElementCollection
    @CollectionTable(name = "MEDIA_LANGUAGE", joinColumns = @JoinColumn(name = "MEDIA_ID"))
    @Column(name = "LANGUAGE")
    private List<String> languages;

    @ElementCollection
    @CollectionTable(name = "MEDIA_EPISODE_RUN_TIME", joinColumns = @JoinColumn(name = "MEDIA_ID"))
    @Column(name = "EPISODE_RUN_TIME")
    private List<Integer> episode_run_time;

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
    private Long revenue;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MEDIA_GENRE", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID", referencedColumnName = "CINEVO_ID"))
    private List<Genre> genres;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "BELONG_TO_COLLECTION_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Collection belong_to_collection;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinTable(name = "MEDIA_ORIGIN_COUNTRY", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "CINEVO_ID"))
    private List<Country> origin_countries;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinTable(name = "MEDIA_PRODUCTION_COMPANY", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCTION_COMPANY_ID", referencedColumnName = "CINEVO_ID"))
    private List<Company> production_companies;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST })
    @JoinTable(name = "MEDIA_PRODUCTION_COUNTRY", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCTION_COUNTRY_ID", referencedColumnName = "CINEVO_ID"))
    private List<ProductionCountry> production_countries;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "MEDIA_SPOKEN_LANGUAGE", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "SPOKEN_LANGUAGE_ID", referencedColumnName = "CINEVO_ID"))
    private List<SpokenLanguage> spoken_languages;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Image> images;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MEDIA_KEYWORD", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "KEYWORD_ID", referencedColumnName = "CINEVO_ID"))
    private List<Keyword> keywords;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Video> videos;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<WatchProvider> watch_providers;

    @ManyToMany(mappedBy = "medias", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Person> created_by;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinTable(name = "MEDIA_NETWORK", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "NETWORK_ID", referencedColumnName = "CINEVO_ID"))
    private List<Network> networks;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    @JsonManagedReference
    private List<Season> seasons;

    @OneToOne(mappedBy = "media", fetch = FetchType.LAZY)
    @JsonManagedReference
    private External external;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "LAST_EPISODE_TO_AIR_CINEVO_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private EpisodeToAir last_episode_to_air;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "NEXT_EPISODE_TO_AIR_CINEVO_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private EpisodeToAir next_episode_to_air;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Credit> credits;

    public final static String TABLE_AS = "media";
    public final static String TABLE_NAME = "MEDIA ";
    public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";

    @JsonProperty("poster_path")
    public String getPoster_path()
    {
        return Job.configurationUrlImages + poster_path;
    }

    @JsonProperty("backdrop_path")
    public String getBackdrop_path()
    {
        return Job.configurationUrlImages + backdrop_path;
    }

    @Data
    @Entity
    @Table(name = "SEASON")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = { "episodes", "images", "videos", "watch_providers", "media" }, callSuper = false)
    public static class Season extends BaseEntity
    {
        @Column(name = "TMDB_ID", nullable = false, unique = true)
        private Integer tmdb_id;

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

        @OneToOne(mappedBy = "season", fetch = FetchType.LAZY)
        @JsonManagedReference
        private External external;

        @OneToMany(mappedBy = "season", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
        @JsonManagedReference
        private List<Episode> episodes;

        @OneToMany(mappedBy = "season", fetch = FetchType.LAZY)
        @JsonIgnoreProperties("hibernateLazyInitializer")
        @JsonManagedReference
        private List<Image> images;

        @OneToMany(mappedBy = "season", fetch = FetchType.LAZY)
        @JsonIgnoreProperties("hibernateLazyInitializer")
        @JsonManagedReference
        private List<Video> videos;

        @OneToMany(mappedBy = "season", fetch = FetchType.LAZY)
        @JsonIgnoreProperties("hibernateLazyInitializer")
        @JsonManagedReference
        private List<WatchProvider> watch_providers;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "MEDIA_ID", nullable = false)
        @JsonBackReference
        private Media media;

        public final static String TABLE_AS = "season";
        public final static String TABLE_NAME = "SEASON ";
        public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";
        public final static String SEASON_NUMBER = TABLE_AS + ".SEASON_NUMBER";
        public final static String JOIN_MEDIA = TABLE_AS + ".MEDIA_ID";
        public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";

        @JsonProperty("poster_path")
        public String getPoster_Path()
        {
            return Job.configurationUrlImages + poster_path;
        }

        @Data
        @Entity
        @Table(name = "EPISODE")
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        @EqualsAndHashCode(exclude = { "season", "videos", "images" }, callSuper = false)
        public static class Episode extends BaseEntity
        {
            @Column(name = "AIR_DATE")
            private LocalDate air_date;

            @Column(name = "EPISODE_NUMBER")
            private Integer episode_number;

            @Column(name = "EPISODE_TYPE")
            @Enumerated(EnumType.STRING)
            private EpisodeType episode_type;

            @Column(name = "TMDB_ID", nullable = false, unique = true)
            private Integer tmdb_id;

            @Lob
            @Column(name = "OVERVIEW", columnDefinition = "TEXT")
            private String overview;

            @Column(name = "RUNTIME")
            private Integer runtime;

            @Column(name = "STILL_PATH")
            private String still_path;

            @Column(name = "VOTE_AVERAGE")
            private Double vote_average;

            @Column(name = "VOTE_COUNT")
            private Integer vote_count;

            @OneToOne(mappedBy = "episode", fetch = FetchType.LAZY)
            @JsonManagedReference
            private External external;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "SEASON_ID", nullable = false)
            @JsonBackReference
            private Season season;

            @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
            @JsonIgnoreProperties("hibernateLazyInitializer")
            @JsonManagedReference
            private List<Image> images;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "PRODUCTION_COUNTRY_ID")
            @JsonBackReference
            private ProductionCountry production_code;

            @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
            @JsonIgnoreProperties("hibernateLazyInitializer")
            @JsonManagedReference
            private List<Video> videos;

            public final static String TABLE_AS = "episode";
            public final static String TABLE_NAME = "EPISODE ";
            public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";
            public final static String SEASON_ID = TABLE_AS + ".SEASON_ID";
            public final static String EPISODE_NUMBER = TABLE_AS + ".EPISODE_NUMBER";
            public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";

            @JsonProperty("still_path")
            public String getStill_path()
            {
                return Job.configurationUrlImages + still_path;
            }
        }
    }

    @Data
    @Entity
    @Table(name = "EPISODE_TO_AIR")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EpisodeToAir extends BaseEntity
    {
        @Column(name = "TMDB_ID", nullable = false, unique = true)
        private Integer tmdb_id;

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

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "PRODUCTION_COUNTRY_ID")
        private ProductionCountry production_country;

        @Column(name = "RUNTIME")
        private Integer runtime;

        @Column(name = "SEASON_NUMBER")
        private Integer season_number;

        @Column(name = "STILL_PATH")
        private String still_path;

        @ManyToOne
        @JoinColumn(name = "MEDIA_ID")
        @JsonIgnoreProperties("hibernateLazyInitializer")
        @JsonBackReference
        private Media media;

        public final static String TABLE_AS = "episode_to_air";
        public final static String TABLE_NAME = "EPISODE_TO_AIR ";
        public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";

        @JsonProperty("still_path")
        public String getStill_path()
        {
            return Job.configurationUrlImages + still_path;
        }
    }
}
