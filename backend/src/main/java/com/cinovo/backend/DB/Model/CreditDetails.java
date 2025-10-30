package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.Gender;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.Enum.Type;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "CREDIT_DETAIL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreditDetails extends BaseEntity {
    @Column(name = "ID", length = 64, nullable = true)
    private String id;

    @Column(name = "CREDIT_TYPE")
    private String credit_type;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "JOB")
    private String job;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "PERSON_ID")
    @JsonManagedReference
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Person person;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "MEDIA_ID")
    @JsonManagedReference
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Media media;

    @Data
    @Entity
    @Table(name = "CREDIT_DETAIL_MEDIA")
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Media extends BaseEntity {
        @Column(name = "ADULT")
        private Boolean adult;

        @Column(name = "ID")
        private Integer id;

        @Column(name = "NAME")
        private String name;

        @Column(name = "ORIGINAL_NAME")
        private String original_name;

        @Column(name = "OVERVIEW", columnDefinition="TEXT")
        private String overview;

        @Column(name = "BACKDROP_PATH")
        private String backdrop_path;

        @Column(name = "POSTER_PATH")
        private String poster_path;

        @Column(name = "TYPE")
        @Enumerated(EnumType.STRING)
        private Type type;

        @Column(name = "ORIGINAL_LANGUAGE")
        private String original_language;

        @ManyToMany
        @JoinTable(
                name = "DETAIL_GENRE",
                joinColumns = @JoinColumn(name = "DETAIL_ID"),
                inverseJoinColumns = @JoinColumn(name = "GENRE_ID")
        )
        private List<Genre> genres;

        @Column(name = "POPULARITY")
        private Double popularity;

        @Column(name = "FIRST_AIR_DATE")
        private String first_air_date;

        @Column(name = "VOTE_AVERAGE")
        private Double vote_average;

        @Column(name = "VOTE_COUNT")
        private Integer vote_count;

        @Column(name = "ORIGIN_COUNTRY")
        private String origin_country;

        @Column(name = "`CHARACTER`")
        private String character;

        @OneToOne(mappedBy = "media", fetch = FetchType.LAZY)
        @JsonBackReference
        private CreditDetails credit_details;

        @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonManagedReference
        private List<CreditDetails.Media.Episode> episodes;

        @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonManagedReference
        private List<CreditDetails.Media.Season> seasons;


        @Data
        @Entity
        @Table(name = "CREDIT_DETAIL_MEDIA_EPISODE")
        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class Episode extends BaseEntity {
            @Column(name = "ID")
            private Integer id;

            @Column(name = "NAME")
            private String name;

            @Column(name = "OVERVIEW", columnDefinition = "TEXT")
            private String overview;

            @Column(name = "MEDIA_TYPE")
            @Enumerated(EnumType.STRING)
            private MediaType media_type;

            @Column(name = "VOTE_AVERAGE")
            private Double vote_average;

            @Column(name = "VOTE_COUNT")
            private Integer vote_count;

            @Column(name = "AIR_DATE")
            private LocalDate air_date;

            @Column(name = "EPISODE_NUMBER")
            private Integer episode_number;

            @Column(name = "EPISODE_TYPE")
            private String episode_type;

            @Column(name = "PRODUCTION_CODE")
            private String production_code;

            @Column(name = "RUNTIME")
            private Integer runtime;

            @Column(name = "SEASON_NUMBER")
            private Integer season_number;

            @Column(name = "SHOW_ID")
            private Integer show_id;

            @Column(name = "STILL_PATH")
            private String still_path;

            @ManyToOne
            @JoinColumn(name = "CREDIT_DETAIL_EPISODES", referencedColumnName = "CINEVO_ID")
            @JsonBackReference
            private Media media;
        }

        @Data
        @Entity
        @Table(name = "CREDIT_DETAIL_MEDIA_SEASON")
        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class Season extends BaseEntity {
            @Column(name = "ID")
            private Integer id;

            @Column(name = "NAME")
            private String name;

            @Column(name = "OVERVIEW", columnDefinition = "TEXT")
            private String overview;

            @Column(name = "POSTER_PATH")
            private String poster_path;

            @Column(name = "media_type")
            @Enumerated(EnumType.STRING)
            private MediaType media_type;

            @Column(name = "VOTE_AVERAGE")
            private Double vote_average;

            @Column(name = "AIR_DATE")
            private LocalDate air_date;

            @Column(name = "SEASON_NUMBER")
            private Integer season_number;

            @Column(name = "SHOW_ID")
            private Integer show_id;

            @Column(name = "EPISODE_COUNT")
            private Integer episode_count;

            @ManyToOne
            @JoinColumn(name = "CREDIT_DETAIL_SEASONS", referencedColumnName = "CINEVO_ID")
            @JsonBackReference
            private Media media;
        }
    }

    @Data
    @Entity
    @Table(name = "CREDIT_DETAIL_PERSON")
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Person extends BaseEntity {
        @Column(name = "TYPE")
        @Enumerated(EnumType.STRING)
        private MediaType media_type;

        @OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
        @JsonBackReference
        private CreditDetails credit_details;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "PERSON_ID",referencedColumnName = "CINEVO_ID")
        private com.cinovo.backend.DB.Model.Person person;
    }

    public final static String TABLE_AS = "credit_details";
    public final static String TABLE_NAME = "CREDIT_DETAIL ";
    public final static String ID = TABLE_AS + ".ID";
}
