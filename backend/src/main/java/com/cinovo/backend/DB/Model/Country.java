package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "COUNTRY")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Country extends BaseEntity
{
    @Column(name = "CODE")
    private String code;

    @Column(name = "Type")
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @OneToMany(mappedBy = "country", cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Certification> certifications;

    public final static String TABLE_AS = "country";
    public final static String TABLE_NAME = "COUNTRY ";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String CODE = TABLE_AS + ".CODE";

    @Entity
    @Data
    @Table(name = "CERTIFICATION")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Certification extends BaseEntity
    {
        @Column(name = "CERTIFICATION")
        private String certification;

        @Column(name = "MEANING", columnDefinition = "TEXT")
        private String meaning;

        @Column(name = "`ORDER`")
        private Integer order;

        @ManyToOne
        @JoinColumn(name = "COUNTRY_CINEVO_ID", referencedColumnName = "CINEVO_ID")
        @JsonBackReference
        private Country country;

        public final static String TABLE_AS = "certification";
        public final static String TABLE_NAME = "CERTIFICATION ";
        public final static String COUNTRY_CINEVO_ID = TABLE_AS + ".COUNTRY_CINEVO_ID";
    }

}
