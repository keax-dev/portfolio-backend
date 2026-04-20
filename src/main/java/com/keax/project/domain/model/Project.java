package com.keax.project.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    private Long projectId;
    private String projectTitle;
    private String projectTitleEs;
    private String projectDescription;
    private String projectDescriptionEs;
    private String projectPicture;
    private String projectDeploy;
    private String projectGithub;
    private int projectPosition;
    private Long technologyId;
    private String technologyName;
    private Boolean projectDeleted;

}
