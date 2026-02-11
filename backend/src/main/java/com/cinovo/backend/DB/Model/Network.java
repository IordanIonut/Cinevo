package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.FileType;
import com.cinovo.backend.DB.Model.Embedded.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "NETWORK")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Network extends BaseEntity
{
    @Column(name = "TMDB_ID", nullable = false, unique = true)
    private Integer tmdb_id;

    @Column(name = "HEADQUARTERS")
    private String headquarters;

    @Column(name = "HOMEPAGE")
    private String homepage;

    @Column(name = "LOGO_PATH")
    private String logo_path;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORIGIN_COUNTRY")
    private String origin_country;

    @OneToMany(mappedBy = "network", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<NetworkAlternativeName> alternative_names;

    @OneToMany(mappedBy = "network", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<NetworkImage> images;

    public final static String TABLE_AS = "network";
    public final static String TABLE_NAME = "NETWORK ";
    public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";

    @Data
    @Entity
    @Table(name = "NETWORK_ALTERNATIVE_NAME")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NetworkAlternativeName extends BaseEntity
    {
        @Column(name = "NAME")
        private String name;

        @Column(name = "TYPE")
        private String type;

        @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
        @JoinColumn(name = "NETWORK_ID")
        @JsonBackReference
        private Network network;
    }

    //TODO: move to image if real need this table
    @Data
    @Entity
    @Table(name = "NETWORK_IMAGE")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NetworkImage extends BaseEntity
    {
        @Column(name = "TMDB_ID", nullable = false, unique = true)
        private String tmdb_id;

        @Column(name = "ASPECT_RATIO")
        private Double aspect_ratio;

        @Column(name = "FILE_PATH")
        private String file_path;

        @Column(name = "HEIGHT")
        private Integer height;

        @Column(name = "WIDTH")
        private Integer width;

        @Column(name = "FILE_TYPE")
        @Enumerated(EnumType.STRING)
        private FileType file_type;

        @Column(name = "VOTE_AVERAGE")
        private Double vote_average;

        @Column(name = "VOTE_COUNT")
        private Integer vote_count;

        @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
        @JoinColumn(name = "NETWORK_ID")
        @JsonBackReference
        private Network network;
    }
}
