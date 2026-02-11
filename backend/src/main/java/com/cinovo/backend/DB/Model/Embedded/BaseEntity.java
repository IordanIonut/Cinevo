package com.cinovo.backend.DB.Model.Embedded;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity implements Serializable {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "CINEVO_ID", updatable = false, nullable = false)
    private String cinevo_id;

    @Column(name = "LAST_UPDATE", nullable = false)
    private LocalDate lastUpdate;

    @PrePersist
    public void createTs() {
        this.lastUpdate = LocalDate.now();
    }

    @PreUpdate
    public void updateTs() {
        this.lastUpdate = LocalDate.now();
    }
}