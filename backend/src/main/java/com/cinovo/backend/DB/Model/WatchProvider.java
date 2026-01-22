package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.ProviderType;
import com.cinovo.backend.DB.Util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "WATCH_PROVIDER", uniqueConstraints = { @UniqueConstraint(columnNames = { "PROVIDER_ID", "MEDIA_ID", "SEASON_ID" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WatchProvider extends BaseEntity
{
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Column(name = "PROVIDER_TYPE")
    @Enumerated(EnumType.STRING)
    private ProviderType provider_type;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "LOGO_PATH")
    private String logo_path;

    //TODO:Find the connection with id
    @Column(name = "PROVIDER_ID")
    private Integer provider_id;

    @Column(name = "PROVIDER_NAME")
    private String provider_name;

    @Column(name = "DISPLAY_PRIORITY")
    private Integer display_priority;

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

    public final static String TABLE_AS = "provider";
    public final static String TABLE_NAME = "WATCH_PROVIDER ";
    public final static String JOIN_MEDIA = TABLE_AS + ".MEDIA_ID";
    public final static String JOIN_MEDIA_SEASON = TABLE_AS + ".SEASON_ID";
    public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String SEASON_ID = TABLE_AS + ".SEASON_ID";
    public final static String PROVIDER_TYPE = TABLE_AS + ".PROVIDER_TYPE";
    public final static String LOCATION = TABLE_AS + ".LOCATION";
    public final static String PROVIDER_ID = TABLE_AS + ".PROVIDER_ID";
}
