package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.CreditType;
import com.cinovo.backend.DB.Model.Embedded.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "CREDIT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Credit extends BaseEntity
{
    @Column(name = "CREDIT_TYPE")
    @Enumerated(EnumType.STRING)
    private CreditType credit_type;

    @Column(name = "CAST_ID")
    private Integer cast_id;

    @Column(name = "`CHARACTER`")
    private String character;

    @Column(name = "`CREDIT_ID`")
    private String credit_id;

    @Column(name = "`ORDER`")
    private Integer order;

    @Column(name = "EPISODE_COUNT")
    private Integer episode_count;

    @Column(name = "FIRST_CREDIT_AIR_DATE")
    private LocalDate first_credit_air_date;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CREDIT_DEPARTMENT", joinColumns = @JoinColumn(name = "CREDIT_CINEVO_ID"))
    @Column(name = "DEPARTMENT")
    private List<String> department;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CREDIT_POSITION", joinColumns = @JoinColumn(name = "CREDIT_CINEVO_ID"))
    @Column(name = "POSITION")
    private List<String> position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDIA_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JsonBackReference
    private Media media;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Person person;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST })
    @JoinTable(name = "CREDIT_ROLE", joinColumns = @JoinColumn(name = "CREDIT_CINEVO_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_CINEVO_ID", referencedColumnName = "CINEVO_ID"))
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST })
    @JoinTable(name = "CREDIT_JOB", joinColumns = @JoinColumn(name = "CREDIT_CINEVO_ID", referencedColumnName = "CINEVO_ID"),
            inverseJoinColumns = @JoinColumn(name = "JOB_CINEVO_ID", referencedColumnName = "CINEVO_ID"))
    private List<Job> jobs;

    public final static String TABLE_AS = "credit";
    public final static String TABLE_NAME = "CREDIT ";
    public final static String MEDIA_ID = TABLE_AS + ".MEDIA_ID";
    public final static String PERSON_ID = TABLE_AS + ".PERSON_ID";
    public final static String CREDIT_ID = TABLE_AS + ".CREDIT_ID";
    public final static String JOIN_MEDIA = MEDIA_ID + " = " + Media.CINEVO_ID;
    public final static String CINEVO_ID = TABLE_AS + ".CINEVO_ID";

    @Data
    @Entity
    @Table(name = "ROLE")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Role extends BaseEntity
    {
        @Column(name = "`CREDIT_ID`")
        private String credit_id;

        @Column(name = "`CHARACTER`")
        private String character;

        @Column(name = "EPISODE_COUNT")
        private Integer episode_count;

        public final static String TABLE_AS = "role";
        public final static String TABLE_NAME = "ROLE ";
        public final static String CREDIT_ID = TABLE_AS + ".CREDIT_ID";

    }

    @Data
    @Entity
    @Table(name = "JOB")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Job extends BaseEntity
    {
        @Column(name = "`CREDIT_ID`")
        private String credit_id;

        @Column(name = "JOB")
        private String job;

        @Column(name = "EPISODE_COUNT")
        private Integer episode_count;

        public final static String TABLE_AS = "job";
        public final static String TABLE_NAME = "JOB ";
        public final static String CREDIT_ID = TABLE_AS + ".CREDIT_ID";
    }
}
