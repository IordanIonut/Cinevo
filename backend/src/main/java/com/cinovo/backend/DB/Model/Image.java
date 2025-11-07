package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.ImageType;
import com.cinovo.backend.Enum.MediaType;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "IMAGE")
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
    private Double aspect_ratio;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDIA_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Media media;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Person person;

    public final static String TABLE_AS = "image";
    public final static String TABLE_NAME = "IMAGE ";
    public final static String ID = TABLE_AS + ".ID";
    public final static String IMAGE_TYPE = TABLE_AS + ".IMAGE_TYPE";
    public final static String JOIN_MEDIA = TABLE_AS + ".MEDIA_ID";
}
