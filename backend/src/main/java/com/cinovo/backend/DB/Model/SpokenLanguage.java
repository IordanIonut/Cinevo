package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Embedded.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Table(name = "SPOKEN_LANGUAGE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpokenLanguage extends BaseEntity
{
    @Column(name = "ENGLISH_NAME")
    private String english_name;

    @Column(name = "ISO_639_1")
    private String iso_639_1;

    @Column(name = "NAME")
    private String name;

    public final static String TABLE_AS = "spoken_language";
    public final static String TABLE_NAME = "SPOKEN_LANGUAGE ";
    public final static String ISO = TABLE_AS + ".ISO_639_1";
}
