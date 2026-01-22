package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.SiteType;
import com.cinovo.backend.DB.Model.Enum.VideoType;
import com.cinovo.backend.DB.Util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "VIDEO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Video extends BaseEntity
{
    @Column(name = "ISO_639_1")
    private String iso_639_1;

    @Column(name = "ISO_3166_1")
    private String iso_3166_1;

    @Column(name = "NAME")
    private String name;

    @Column(name = "`KEY`")
    private String key;

    @Column(name = "SITE")
    @Enumerated(EnumType.STRING)
    private SiteType site;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private VideoType type;

    @Column(name = "OFFICIAL")
    private Boolean official;

    @Column(name = "PUBLISHED_AT")
    private LocalDate published_at;

    @Column(name = "TMDB_ID", nullable = false, unique = true)
    private String tmdb_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDIA_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Media media;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEASON_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
        private Media.Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EPISODE_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Media.Season.Episode episode;

    public final static String TABLE_AS = "video";
    public final static String TABLE_NAME = "VIDEO ";
    public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";
    public final static String SEASON_ID =  TABLE_AS + ".SEASON_ID";
    public final static String JOIN_MEDIA = TABLE_AS + ".MEDIA_ID";
    public final static String CINEVO_ID =  TABLE_AS + ".CINEVO_ID";
}
