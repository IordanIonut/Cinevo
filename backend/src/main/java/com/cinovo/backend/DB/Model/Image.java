package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@Entity
@Table(name = "IMAGE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Image extends BaseEntity {
    @Column(name = "ID")
    private Integer id;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<BackDrop> backdrops;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Poster> posters;

    public final static String TABLE_AS = "image";
    public final static String TABLE_NAME = "IMAGE ";
    public final static String ID = TABLE_AS + ".ID";

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Entity
    @Table(name = "BACK_DROP")
    public static class BackDrop extends BaseEntity{
        @Column(name = "ASPECT_RADIO")
        private Double aspect_radio;

        @Column(name = "HEIGHT")
        private Long height;

        @Column(name = "WIDTH")
        private Long width;

        @Column(name = "ISO")
        private String iso;

        @Column(name = "FILE_PATH")
        private String file_path;

        @Column(name = "VOTE_AVERAGE")
        private Double vote_average;

        @Column(name = "VOTE_COUNT")
        private Long vote_count;

        @ManyToOne
        @JoinColumn(name = "IMAGE_CINEVO_ID", referencedColumnName = "CINEVO_ID")
        @JsonBackReference
        private Image image;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Entity
    @Table(name = "POSTER")
    public static class Poster extends BaseEntity{
        @Column(name = "ASPECT_RADIO")
        private Double aspect_radio;

        @Column(name = "HEIGHT")
        private Long height;

        @Column(name = "WIDTH")
        private Long width;

        @Column(name = "ISO")
        private String iso;

        @Column(name = "FILE_PATH")
        private String file_path;

        @Column(name = "VOTE_AVERAGE")
        private Double vote_average;

        @Column(name = "VOTE_COUNT")
        private Long vote_count;

        @ManyToOne
        @JoinColumn(name = "IMAGE_CINEVO_ID", referencedColumnName = "CINEVO_ID")
        @JsonBackReference
        private Image image;
    }
}
