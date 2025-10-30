package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "TV")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TV extends BaseEntity {
    @Column(name = "ADULT")
    private Boolean adult;

    @Column(name = "BACKDROP_PATH")
    private String backdrop_path;

    @Column(name = "ID")
    private Integer id;

    @Column(name = "ORIGIN_COUNTY")
    private String origin_country;

    @Column(name = "ORIGINAL_LANGUAGE")
    private String original_language;

    @Column(name = "ORIGINAL_NAME")
    private String original_name;

    @Column(name = "OVERVIEW", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "POPULARITY")
    private Double popularity;

    @Column(name = "POSTER_PATH")
    private String poster_path;

    @Column(name = "FIRST_AIR_DATE")
    private LocalDate first_air_date;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VOTE_AVERAGE")
    private Double vote_average;

    @Column(name = "VOTE_COUNT")
    private Integer vote_count;

    @ManyToMany
    @JoinTable(
            name = "TV_GENRE",
            joinColumns = @JoinColumn(name = "TV_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID")
    )
    private List<Genre> genres;

    public final static String TABLE_AS = "tv";
    public final static String TABLE_NAME = "TV ";
    public final static String ID = TABLE_AS + ".ID";
}
