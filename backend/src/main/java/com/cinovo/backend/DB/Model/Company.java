package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Schedule.Job;
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
public class Company extends BaseEntity
{
    @Column(name = "TMDB_ID", nullable = false, unique = true)
    private Integer tmdb_id;

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
    public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";

    @JsonProperty("logo_path")
    public String getLogo_path()
    {
        return Job.configurationUrlImages + logo_path;
    }
}
