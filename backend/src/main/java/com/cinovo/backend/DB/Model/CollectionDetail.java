package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.Type;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "COLLECTION_DETAIL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionDetail extends BaseEntity {
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "OVERVIEW")
    private String overview;

    @Column(name = "POSTER_PATH")
    private String poster_path;

    @Column(name = "BACKDROP_PATH")
    private String backdrop_path;

    @OneToMany(mappedBy = "detail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Part> parts;

    public final static String TABLE_AS = "collection";
    public final static String TABLE_NAME = "COLLECTION_DETAIL ";
    public final static String ID = TABLE_AS + ".ID";

    @JsonProperty("poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    @JsonProperty("backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }

    @Data
    @Entity
    @Table(name = "PART")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Part extends  BaseEntity {
        @Column(name = "ADULT")
        private Boolean adult;

        @Column(name = "BACKDROP_PATH")
        private String backdrop_path;

        @Column(name = "ID")
        private Integer id;

        @Column(name = "TITLE")
        private String title;

        @Column(name = "ORIGINAL_TITLE")
        private String original_title;

        @Column(name = "OVERVIEW", columnDefinition = "TEXT")
        private String overview;

        @Column(name = "POSTER_PATH")
        private String poster_path;

        @Column(name = "MEDIA_TYPE")
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

        @Column(name = "RELEASE_DATE")
        private LocalDate release_date;

        @Column(name = "VIDEO")
        private Boolean video;

        @Column(name = "VOTE_AVERAGE")
        private Double vote_average;

        @Column(name = "VOTE_COUNT")
        private Integer vote_count;

        @ManyToOne
        @JoinColumn(name = "DETAIL_CINEVO_ID", referencedColumnName = "CINEVO_ID")
        @JsonBackReference
        private CollectionDetail detail;

        @JsonProperty("poster_path")
        public String getPoster_path() {
            return poster_path;
        }

        @JsonProperty("backdrop_path")
        public String getBackdrop_path() {
            return backdrop_path;
        }
    }
}
