package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.CreditType;
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

    @Column(name = "CREDIT_ID")
    private String credit_id;

    @Column(name = "`ORDER`")
    private Integer order;

    @Column(name = "EPISODE_COUNT")
    private Integer episode_count;

    @Column(name = "FIRST_CREDIT_AIR_DATE")
    private LocalDate first_credit_air_date;

    @ElementCollection
    @CollectionTable(name = "CREDIT_DEPARTMENT", joinColumns = @JoinColumn(name = "CINEVO_ID"))
    @Column(name = "DEPARTMENT")
    private List<String> department;

    @ElementCollection
    @CollectionTable(name = "CREDIT_JOB", joinColumns = @JoinColumn(name = "CINEVO_ID"))
    @Column(name = "JOB")
    private List<String> job;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Media_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Media media;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Person person;

    public final static String TABLE_AS = "credit";
    public final static String TABLE_NAME = "CREDIT ";
    public final static String MEDIA_ID = TABLE_AS + ".MEDIA_ID";
    public final static String PERSON_ID = TABLE_AS + ".PERSON_ID";
    public final static String CREDIT_ID =  TABLE_AS + ".CREDIT_ID";
    public final static String JOIN_MEDIA = MEDIA_ID + " = " + Media.CINEVO_ID;
}
