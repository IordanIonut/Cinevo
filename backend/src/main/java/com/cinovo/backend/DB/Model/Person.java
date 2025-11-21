package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "PERSON")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person extends BaseEntity
{
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ADULT")
    private Boolean adult;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "KNOWN_FOR_DEPARTMENT")
    private String known_for_department;

    @Column(name = "ORIGINAL_NAME")
    private String original_name;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PROFILE_FILE")
    private String profile_file;

    @Column(name = "POPULARITY")
    private Double popularity;

    @ElementCollection
    @CollectionTable(name = "MEDIA_ALSO_KNOW_AS", joinColumns = @JoinColumn(name = "KNOW_AS"))
    @Column(name = "ALSO_KNOW_AS")
    private List<String> also_known_as;

    @Lob
    @Column(name = "BIOGRAPHY", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    @Column(name = "DEATHDAY")
    private LocalDate deathday;

    @Column(name = "HOMEPAGE")
    private String homepage;

    @Column(name = "FREEBASE_MID")
    private String freebase_mid;

    @Column(name = "FREEBASE_ID")
    private String freebase_id;

    @Column(name = "IMDB_ID")
    private String imdb_id;

    @Column(name = "TVRAGE_ID")
    private String tvrage_id;

    @Column(name = "WIKIDATA_ID")
    private String wikidata_id;

    @Column(name = "FACEBOOK_ID")
    private String facebook_id;

    @Column(name = "TWITTER_ID")
    private String twitter_id;

    @Column(name = "YOUTUBE_ID")
    private String youtube_id;

    @Column(name = "INSTAGRAM_ID")
    private String instagram_id;

    @Column(name = "TIKTOK_ID")
    private String tiktok_id;

    @Column(name = "PLACE_OF_BIRTH")
    private String place_of_birth;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PERSON_MEDIA", joinColumns = @JoinColumn(name = "PERSON_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"))
    @JsonBackReference
    private List<Media> medias;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Image> images;

    public final static String TABLE_AS = "person";
    public final static String TABLE_NAME = "PERSON ";
    public final static String ID = TABLE_AS + ".ID";
    public final static String PERSON_MEDIA_NAME = "PERSON_MEDIA ";
    public final static String PERSON_MEDIA_AS = "person_media";
    public final static String MEDIA_ID = PERSON_MEDIA_AS + ".MEDIA_ID";
    public final static String PERSON_ID = PERSON_MEDIA_AS + ".PERSON_ID";
    public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";
}
