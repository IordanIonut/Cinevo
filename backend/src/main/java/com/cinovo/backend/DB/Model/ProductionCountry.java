package com.cinovo.backend.DB.Model;

import com.cinovo.backend.DB.Util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Table(name = "PRODUCTION_COUNTRY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductionCountry extends BaseEntity
{
    @Column(name = "ISO_3166_1")
    private String iso_3166_1;

    @Column(name = "NAME")
    private String name;

    public final static String TABLE_AS = "production_country";
    public final static String TABLE_NAME = "PRODUCTION_COUNTRY ";
    public final static String ISO = TABLE_AS + ".ISO_3166_1";
}
