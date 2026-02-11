package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.ImageType;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Embedded.BaseEntity;
import com.cinovo.backend.Schedule.Job;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "IMAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "TMDB_ID", "FILE_PATH" }) })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Image extends BaseEntity
{
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Column(name = "ASPECT_RADIO")
    private Double aspect_radio;

    @Column(name = "HEIGHT")
    private Integer height;

    @Column(name = "ISO_3166_1")
    private String iso_3166_1;

    @Column(name = "ISO_639_1")
    private String iso_639_1;

    @Column(name = "FILE_PATH")
    private String file_path;

    @Column(name = "VOTE_AVERAGE")
    private Double vote_average;

    @Column(name = "VOTE_COUNT")
    private Integer vote_count;

    @Column(name = "WIDTH")
    private Integer width;

    @Column(name = "IMAGE_TYPE")
    @Enumerated(EnumType.STRING)
    private ImageType image_type;

    //collection id create and connect
    @Column(name = "COLLECTION_ID")
    private Integer collection_id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "MEDIA_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Media media;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "SEASON_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Media.Season season;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "EPISODE_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Media.Season.Episode episode;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "PERSON_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Person person;

    public final static String TABLE_AS = "image";
    public final static String TABLE_NAME = "IMAGE ";
    public final static String IMAGE_TYPE = TABLE_AS + ".IMAGE_TYPE";
    public final static String MEDIA_ID = TABLE_AS + ".MEDIA_ID";
    public final static String SEASON_ID = TABLE_AS + ".SEASON_ID";
    public final static String EPISODE_ID = TABLE_AS + ".EPISODE_ID";
    public final static String COLLECTION_ID = TABLE_AS + ".COLLECTION_ID";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String PERSON_ID = TABLE_AS + ".PERSON_ID";
    public final static String FILE_PATH = TABLE_AS + ".FILE_PATH";

    @JsonProperty("file_path")
    public String gerFile_path()
    {
        return Job.configurationUrlImages + file_path;
    }
}
