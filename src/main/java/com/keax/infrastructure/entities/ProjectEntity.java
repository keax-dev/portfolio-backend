package com.keax.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "project")
@Data
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

    @Column(name = "project_deploy")
    private String projectDeploy;

    @Column(name = "project_github")
    private String projectGithub;

    @Column(name = "project_position", nullable = false)
    private int projectPosition;

    @Column(name = "project_deleted", nullable = false)
    private Boolean projectDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_id", nullable = false)
    private TechnologyEntity technology;

}
