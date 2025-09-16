package com.keax.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "technology")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technology_id")
    private Long technologyId;

    @Column(name = "technology_name", nullable = false)
    private String technologyName;

    @Column(name = "technology_deleted", nullable = false)
    private Boolean technologyDeleted;

}
