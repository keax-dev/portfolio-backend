package com.keax.project.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

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
    private int projectPosition;
    private Boolean projectDeleted;
    private List<ProjectTechnology> projectTechnologies = new ArrayList<>();
    private List<ProjectLink> projectLinks = new ArrayList<>();
    private List<ProjectImage> projectImages = new ArrayList<>();

}
