package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Embedded.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "GENRE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genre extends BaseEntity
{
    @Column(name = "TMDB_ID", nullable = false)
    private Integer tmdb_id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private MediaType type;

    public final static String TABLE_AS = "gen";
    public final static String TABLE_NAME = "GENRE ";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String TMDB_ID = TABLE_AS + ".TMDB_ID";
}
