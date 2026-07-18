package com.keax.technology.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "technology")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technology_id")
    private Long technologyId;

    @Column(name = "technology_name", nullable = false)
    private String technologyName;

    @Column(name = "technology_position", nullable = false)
    private int technologyPosition;

    @Column(name = "technology_deleted", nullable = false)
    private Boolean technologyDeleted;

}
