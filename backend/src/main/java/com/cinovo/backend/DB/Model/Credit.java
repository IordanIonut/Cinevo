package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.CreditType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "JOB")
    private String job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Movie movie;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "CINEVO_ID")
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Person person;

    public final static String TABLE_AS = "credit";
    public final static String TABLE_NAME = "CREDIT ";
    public final static String MOVIE_ID = TABLE_AS + ".MOVIE_ID";
    public final static String CREDIT_ID =  TABLE_AS + ".CREDIT_ID";
    public final static String JOIN_MOVIE = MOVIE_ID + " = " + Movie.CINEVO_ID;
}
