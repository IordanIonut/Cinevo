package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.Gender;
import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Schedule.Job;
import com.fasterxml.jackson.annotation.*;
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
    @Column(name = "TMDB_ID", nullable = false, unique = true)
    private Integer tmdb_id;

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
    @CollectionTable(name = "PERSON_ALSO_KNOW_AS", joinColumns = @JoinColumn(name = "PERSON_CINEVO_ID"))
    @Column(name = "ALSO_KNOW_AS")
    private List<String> also_know_as;

    @Lob
    @Column(name = "BIOGRAPHY", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    @Column(name = "DEATHDAY")
    private LocalDate deathday;

    @Column(name = "PLACE_OF_BIRTH")
    private String place_of_birth;

    @Column(name = "HOMEPAGE")
    private String homepage;

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
    @JsonManagedReference
    private External external;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinTable(name = "PERSON_MEDIA", joinColumns = @JoinColumn(name = "PERSON_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID"))
    @JsonBackReference
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private List<Media> medias;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonManagedReference
    private List<Image> images;

    public final static String TABLE_AS = "person";
    public final static String TABLE_NAME = "PERSON ";
    public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";
    public final static String PERSON_MEDIA_NAME = "PERSON_MEDIA ";
    public final static String PERSON_MEDIA_AS = "person_media";
    public final static String MEDIA_ID = PERSON_MEDIA_AS + ".MEDIA_ID";
    public final static String PERSON_ID = PERSON_MEDIA_AS + ".PERSON_ID";
    public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";

    @JsonProperty("profile_file")
    public String getProfile_file()
    {
        return Job.configurationUrlImages + profile_file;
    }
}
