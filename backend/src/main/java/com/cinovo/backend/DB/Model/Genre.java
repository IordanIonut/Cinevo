package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import com.cinovo.backend.Enum.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "Type")
    @Enumerated(EnumType.STRING)
    private Type type;

    public final static String TABLE_AS = "gen";
    public final static String TABLE_NAME = "GENRE ";
    public final static String TYPE = TABLE_AS + ".TYPE";
    public final static String ID = TABLE_AS + ".ID";
}
