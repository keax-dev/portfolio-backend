package com.keax.project.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    @JsonProperty("id")
    private Long projectId;

    @JsonProperty("title")
    private String projectTitle;

    @JsonProperty("title_es")
    private String projectTitleEs;

    @JsonProperty("description")
    private String projectDescription;

    @JsonProperty("description_es")
    private String projectDescriptionEs;

    @JsonProperty("position")
    private int projectPosition;

    @JsonProperty("deleted")
    private Boolean projectDeleted;

    @JsonProperty("published")
    private Boolean projectPublished;

    @JsonProperty("technologies")
    private List<ProjectTechnologyDTO> technologies = new ArrayList<>();

    @JsonProperty("links")
    private List<ProjectLinkDTO> links = new ArrayList<>();

    @JsonProperty("images")
    private List<ProjectImageDTO> images = new ArrayList<>();

}
