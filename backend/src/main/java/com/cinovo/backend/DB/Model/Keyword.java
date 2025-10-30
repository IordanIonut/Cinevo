package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Table(name = "KEYWORD")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Keyword extends BaseEntity {
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    public final static String TABLE_AS = "keyword";
    public final static String TABLE_NAME = "KEYWORD ";
    public final static String ID = TABLE_AS +".ID";
}
