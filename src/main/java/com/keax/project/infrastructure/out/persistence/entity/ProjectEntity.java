package com.keax.project.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Setter;
import lombok.Getter;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_title", nullable = false)
    private String projectTitle;

    @Column(name = "project_title_es")
    private String projectTitleEs;

    @Column(name = "project_description", columnDefinition = "text",  nullable = false)
    private String projectDescription;

    @Column(name = "project_description_es", columnDefinition = "text")
    private String projectDescriptionEs;

    @Column(name = "project_picture")
    private String projectPicture;

    @Column(name = "project_position", nullable = false)
    private int projectPosition;

    @Column(name = "project_deleted", nullable = false)
    private Boolean projectDeleted;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectTechnologyEntity> projectTechnologies = new LinkedHashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectLinkEntity> projectLinks = new LinkedHashSet<>();

}
