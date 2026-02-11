package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Embedded.WatchProviderMediaId;
import com.cinovo.backend.DB.Model.Embedded.WatchProviderSeasonId;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.ProviderType;
import com.cinovo.backend.DB.Model.Embedded.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "WATCH_PROVIDER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class WatchProvider extends BaseEntity
{
    @Column(name = "TMDB_ID", nullable = false, unique = true)
    private Integer tmdb_id;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "LOGO_PATH")
    private String logo_path;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DISPLAY_PRIORITY")
    private Integer display_priority;

    @OneToMany(mappedBy = "watch_provider_id", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private List<WatchProviderMedia> media_links;

    @OneToMany(mappedBy = "watch_provider_id", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private List<WatchProviderSeason> season_links;

    @Data
    @Entity
    @Table(name = "WATCH_PROVIDER_MEDIA")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WatchProviderMedia
    {
        @EmbeddedId
        private WatchProviderMediaId id;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("watch_provider_id")
        @JoinColumn(name = "WATCH_PROVIDER_ID")
        @JsonBackReference
        private WatchProvider watch_provider_id;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("media_id")
        @JoinColumn(name = "MEDIA_ID")
        @JsonBackReference
        private Media media_id;

        @Column(name = "MEDIA_TYPE")
        @Enumerated(EnumType.STRING)
        private MediaType media_type;

        @Column(name = "TYPE")
        @Enumerated(EnumType.STRING)
        private ProviderType type;
    }

    @Data
    @Entity
    @Table(name = "WATCH_PROVIDER_SEASON")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WatchProviderSeason
    {
        @EmbeddedId
        private WatchProviderSeasonId id;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("watch_provider_id")
        @JoinColumn(name = "WATCH_PROVIDER_ID")
        @JsonBackReference
        private WatchProvider watch_provider_id;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("season_id")
        @JoinColumn(name = "SEASON_ID")
        @JsonBackReference
        private Media.Season season_id;

        @Column(name = "MEDIA_TYPE")
        @Enumerated(EnumType.STRING)
        private MediaType media_type;

        @Column(name = "TYPE")
        @Enumerated(EnumType.STRING)
        private ProviderType type;
    }

    //    public final static String TABLE_AS = "provider";
    //    public final static String TABLE_NAME = "WATCH_PROVIDER ";
    //    public final static String JOIN_MEDIA = TABLE_AS + ".MEDIA_ID";
    //    public final static String JOIN_MEDIA_SEASON = TABLE_AS + ".SEASON_ID";
    //    public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";
    //    public final static String TYPE = TABLE_AS + ".TYPE";
    //    public final static String SEASON_ID = TABLE_AS + ".SEASON_ID";
    //    public final static String PROVIDER_TYPE = TABLE_AS + ".PROVIDER_TYPE";
    //    public final static String LOCATION = TABLE_AS + ".LOCATION";
    //    public final static String PROVIDER_ID = TABLE_AS + ".PROVIDER_ID";
}
