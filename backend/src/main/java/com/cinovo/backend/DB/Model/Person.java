package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.Gender;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "PERSON")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person extends BaseEntity
{
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ADULT")
    private Boolean adult;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "KNOW_FOR_DEPARTMENT")
    private String know_for_department;

    @Column(name = "ORIGINAL_NAME")
    private String original_name;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PROFILE_FILE")
    private String profile_file;

    @Column(name = "POPULARITY")
    private Double popularity;

    public final static String TABLE_AS = "person";
    public final static String TABLE_NAME = "PERSON ";
    public final static String ID = TABLE_AS + ".ID";
}
