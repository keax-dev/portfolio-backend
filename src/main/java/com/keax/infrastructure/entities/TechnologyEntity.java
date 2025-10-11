package com.keax.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "technology_position", nullable = false)
    private int technologyPosition;

    @Column(name = "technology_deleted", nullable = false)
    private Boolean technologyDeleted;

    @OneToMany(mappedBy = "technology", fetch = FetchType.LAZY)
    private List<ProjectEntity> projectEntityList = new ArrayList<>();

}
