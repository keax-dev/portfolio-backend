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
    private Boolean projectPublished;
    private List<ProjectTechnology> projectTechnologies = new ArrayList<>();
    private List<ProjectLink> projectLinks = new ArrayList<>();
    private List<ProjectImage> projectImages = new ArrayList<>();
    private Long version;

    public Project(
            Long projectId,
            String projectTitle,
            String projectTitleEs,
            String projectDescription,
            String projectDescriptionEs,
            int projectPosition,
            List<ProjectTechnology> projectTechnologies,
            List<ProjectLink> projectLinks,
            List<ProjectImage> projectImages
    ) {
        this(
                projectId,
                projectTitle,
                projectTitleEs,
                projectDescription,
                projectDescriptionEs,
                projectPosition,
                true,
                projectTechnologies,
                projectLinks,
                projectImages,
                null
        );
    }

    public Project(
            Long projectId,
            String projectTitle,
            String projectTitleEs,
            String projectDescription,
            String projectDescriptionEs,
            int projectPosition,
            Boolean projectPublished,
            List<ProjectTechnology> projectTechnologies,
            List<ProjectLink> projectLinks,
            List<ProjectImage> projectImages
    ) {
        this(
                projectId,
                projectTitle,
                projectTitleEs,
                projectDescription,
                projectDescriptionEs,
                projectPosition,
                projectPublished,
                projectTechnologies,
                projectLinks,
                projectImages,
                null
        );
    }

}
