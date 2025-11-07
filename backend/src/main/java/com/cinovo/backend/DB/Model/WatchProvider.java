package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.Enum.ProviderType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "WATCH_PROVIDER")
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEDIA_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Media media;

    public final static String TABLE_AS = "provider";
    public final static String TABLE_NAME = "WATCH_PROVIDER ";
    public final static String ID = TABLE_AS + ".ID";
    public final static String JOIN_MEDIA = TABLE_AS + ".MEDIA_ID";
    public final static String CINEVO_ID =  TABLE_AS + ".CINEVO_ID";
}
