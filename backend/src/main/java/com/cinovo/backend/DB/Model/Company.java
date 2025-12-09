package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "COMPANY")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Company extends BaseEntity {
    @Column(name = "ID")
    private Integer id;

    @Lob
    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

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

    @OneToOne
    private Company parent_company;

    public final static String TABLE_AS = "company";
    public final static String TABLE_NAME = "COMPANY ";
    public final static String ID = TABLE_AS + ".ID";

    @JsonProperty("logo_path")
    public String getLogo_path() {
        return logo_path; ///to add master data provider with configuration data
    }
}
