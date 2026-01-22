package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "TRANSLATE", uniqueConstraints = { @UniqueConstraint(columnNames = { "TMDB_ID", "ISO_LOWER", "ISO_UPPER" }) })
public class Translate extends BaseEntity
{
    @Column(name = "TMDB_ID", nullable = false)
    private Integer tmdb_id;

    @Column(name = "ISO_UPPER", nullable = false)
    private String iso_upper;

    @Column(name = "ISO_LOWER", nullable = false)
    private String iso_lower;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ENGLISH_NAME")
    private String english_name;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "OVERVIEW")
    private String overview;

    @Column(name = "HOME_PAGE")
    private String home_page;

    public final static String TABLE_AS = "translate";
    public final static String TABLE_NAME = "TRANSLATE ";
    public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";
    public final static String ISO_UPPER = TABLE_AS + ".ISO_UPPER";
}
