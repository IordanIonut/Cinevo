package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.MediaType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "GENRE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genre extends BaseEntity {
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private MediaType type;

    public final static String TABLE_AS = "gen";
    public final static String TABLE_NAME = "GENRE ";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String ID = TABLE_AS + ".ID";
}
