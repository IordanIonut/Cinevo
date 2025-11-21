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
@Table(name = "TRANSLATE")
public class Translate extends BaseEntity
{
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ISO_UPPER")
    private String iso_upper;

    @Column(name = "ISO_LOWER")
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
    public final static String ID = TABLE_AS + ".ID";
    public final static String ISO_UPPER = TABLE_AS + ".ISO_UPPER";
}
