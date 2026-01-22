package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Schedule.Job;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "COLLECTION")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Collection extends BaseEntity
{
    @Column(name = "TMDB_ID", nullable = false, unique = true)
    private Integer tmdb_id;

    @Column(name = "NAME")
    private String name;

    @Lob
    @Column(name = "OVERVIEW", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "POSTER_PATH")
    private String poster_path;

    @Column(name = "BACKDROP_PATH")
    private String backdrop_path;

    @Column(name = "ORIGINAL_LANGUAGE")
    private String original_language;

    @Column(name = "ORIGINAL_NAME")
    private String original_name;

    @Column(name = "ADULT")
    private Boolean adult;

    @OneToMany(mappedBy = "belong_to_collection")
    @JsonManagedReference
    private List<Media> medias;

    public final static String TABLE_AS = "collection";
    public final static String TABLE_NAME = "COLLECTION ";
    public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";

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
}
