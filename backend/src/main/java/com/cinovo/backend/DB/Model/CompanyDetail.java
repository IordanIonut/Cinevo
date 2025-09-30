package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "COMPANY_DETAIL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyDetail extends BaseEntity {
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DESCRIPTION")
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
    private CompanyDetail parent_company;

    public final static String TABLE_AS = "company";
    public final static String TABLE_NAME = "COMPANY_DETAIL ";
    public final static String ID = TABLE_AS + ".ID";
}
