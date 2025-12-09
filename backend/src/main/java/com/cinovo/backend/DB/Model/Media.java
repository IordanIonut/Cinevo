package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.EpisodeType;
import com.cinovo.backend.Enum.MediaStatus;
import com.cinovo.backend.Enum.MediaType;
import com.fasterxml.jackson.annotation.*;
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
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @Column(name = "FREEBASE_MID")
    private String freebase_mid;

    @Column(name = "FREEBASE_ID")
    private String freebase_id;

    @Column(name = "TVDB_ID")
    private Integer tvdb_id;

    @Column(name = "TVRAGE_ID")
    private String tvrage_id;

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
    @JoinTable(name = "MEDIA_PRODUCTION_COMPANY", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCTION_COMPANY_ID", referencedColumnName = "CINEVO_ID"))
    private List<Company> production_companies;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinTable(name = "MEDIA_PRODUCTION_COUNTRY", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCTION_COUNTRY_ID", referencedColumnName = "CINEVO_ID"))
    private List<ProductionCountry> production_countries;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
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

    @ManyToOne
    @JoinColumn(name = "DETAIL_CINEVO_ID", referencedColumnName = "CINEVO_ID")
    @JsonBackReference
    private Collection detail;

    @ManyToMany(mappedBy = "medias", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Person> created_by;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinTable(name = "MEDIA_NETWORK", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "NETWORK_ID", referencedColumnName = "CINEVO_ID"))
    private List<Network> networks;

    //    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    //    @JoinTable(name = "MEDIA_SEASON", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
    //            inverseJoinColumns = @JoinColumn(name = "SEASON_ID", referencedColumnName = "CINEVO_ID"))
    //    @JsonIgnoreProperties({ "hibernateLazyInitializer" })
    //    private List<Season> seasons;

    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    @JsonManagedReference
    private List<Season> seasons;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "LAST_EPISODE_TO_AIR_CINEVO_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private EpisodeToAir last_episode_to_air;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "NEXT_EPISODE_TO_AIR_CINEVO_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private EpisodeToAir next_episode_to_air;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinTable(name = "MEDIA_CREDIT", joinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "CREDIT_ID", referencedColumnName = "CINEVO_ID"))
    @JsonManagedReference
    private List<Credit> credits;

    public final static String TABLE_AS = "media";
    public final static String TABLE_NAME = "MEDIA ";
    public final static String ID = TABLE_AS + ".ID";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";

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

        @Column(name = "IMDB_ID")
        private String imdb_id;

        @Column(name = "FREEBASE_MID")
        private String freebase_mid;

        @Column(name = "FREEBASE_ID")
        private String freebase_id;

        @Column(name = "TVDB_ID")
        private Integer tvdb_id;

        @Column(name = "TVRAGE_ID")
        private String tvrage_id;

        @Column(name = "WIKIDATA_ID")
        private String wikidata_id;

        @Column(name = "FACEBOOK_ID")
        private String facebook_id;

        @Column(name = "INSTAGRAM_ID")
        private String instagram_id;

        @Column(name = "TWITTER_ID")
        private String twitter_id;

        //        @OneToMany(mappedBy = "season", fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
        //        @JsonIgnoreProperties("hibernateLazyInitializer")
        //        @JsonManagedReference
        //        private List<Episode> episodes;

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

        //        @ManyToOne(fetch = FetchType.LAZY)
        //        @JoinColumn(name = "MEDIA_ID")
        //        @JsonIgnoreProperties("hibernateLazyInitializer")
        //        @JsonBackReference
        //        private Media media;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "MEDIA_ID", nullable = false)
        @JsonBackReference
        private Media media;

        public final static String TABLE_AS = "season";
        public final static String TABLE_NAME = "SEASON ";
        public final static String ID = TABLE_AS + ".ID";
        public final static String SEASON_NUMBER = TABLE_AS + ".SEASON_NUMBER";
        public final static String JOIN_MEDIA = TABLE_AS + ".MEDIA_ID";
        public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";

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

            @Column(name = "ID")
            private Integer id;

            @Lob
            @Column(name = "OVERVIEW", columnDefinition = "TEXT")
            private String overview;

            @Column(name = "PRODUCTION_CODE")
            private String production_code;

            @Column(name = "RUNTIME")
            private Integer runtime;

            @Column(name = "STILL_PATH")
            private String still_path;

            @Column(name = "VOTE_AVERAGE")
            private Double vote_average;

            @Column(name = "VOTE_COUNT")
            private Integer vote_count;

            @Column(name = "IMDB_ID")
            private String imdb_id;

            @Column(name = "FREEBASE_MID")
            private String freebase_mid;

            @Column(name = "FREEBASE_ID")
            private String freebase_id;

            @Column(name = "TVDB_ID")
            private Integer tvdb_id;

            @Column(name = "TVRAGE_ID")
            private String tvrage_id;

            @Column(name = "WIKIDATA_ID")
            private String wikidata_id;

            @Column(name = "FACEBOOK_ID")
            private String facebook_id;

            @Column(name = "INSTAGRAM_ID")
            private String instagram_id;

            @Column(name = "TWITTER_ID")
            private String twitter_id;

            //            @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
            //            @JoinColumn(name = "SEASON_ID")
            //            @JsonIgnoreProperties("hibernateLazyInitializer")
            //            @JsonBackReference
            //            private Season season;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "SEASON_ID", nullable = false)
            @JsonBackReference
            private Season season;

            @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
            @JsonIgnoreProperties("hibernateLazyInitializer")
            @JsonManagedReference
            private List<Image> images;

            @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
            @JsonIgnoreProperties("hibernateLazyInitializer")
            @JsonManagedReference
            private List<Video> videos;

            public final static String TABLE_AS = "episode";
            public final static String TABLE_NAME = "EPISODE ";
            public final static String ID = TABLE_AS + ".ID";
            public final static String SEASON_ID = TABLE_AS + ".SEASON_ID";
            public final static String EPISODE_NUMBER = TABLE_AS + ".EPISODE_NUMBER";
            public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";
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

        public final static String TABLE_AS = "episode_to_air";
        public final static String TABLE_NAME = "EPISODE_TO_AIR ";
        public final static String ID = TABLE_AS + ".ID";
    }
}
