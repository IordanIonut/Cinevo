package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Embedded.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "EXTERNAL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class External extends BaseEntity
{
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

    @Column(name = "YOUTUBE_ID")
    private String youtube_id;

    @Column(name = "TIKTOK_ID")
    private String tiktok_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDIA_ID")
    @JsonBackReference

    private Media media;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID")
    @JsonBackReference

    private Person person;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEASON_ID")
    @JsonBackReference
    private Media.Season season;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EPISODE_ID")
    @JsonBackReference
    private Media.Season.Episode episode;
}
