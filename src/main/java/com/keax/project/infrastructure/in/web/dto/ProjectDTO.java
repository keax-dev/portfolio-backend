package com.keax.project.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
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
    @NotBlank(message = "The project title is required")
    @Size(max = 160, message = "The project title must not exceed 160 characters")
    private String projectTitle;

    @JsonProperty("title_es")
    @NotBlank(message = "The project es title is required")
    @Size(max = 160, message = "The project es title must not exceed 160 characters")
    private String projectTitleEs;

    @JsonProperty("description")
    @NotBlank(message = "The project description is required")
    @Size(max = 10000, message = "The project description must not exceed 10000 characters")
    private String projectDescription;

    @JsonProperty("description_es")
    @NotBlank(message = "The project es description is required")
    @Size(max = 10000, message = "The project es description must not exceed 10000 characters")
    private String projectDescriptionEs;

    @JsonProperty("position")
    @Min(value = 1, message = "The project position must be greater than 0")
    private int projectPosition;

    @JsonProperty("deleted")
    private Boolean projectDeleted;

    @JsonProperty("technologies")
    @Valid
    @NotEmpty(message = "At least one project technology is required")
    private List<ProjectTechnologyDTO> technologies = new ArrayList<>();

    @JsonProperty("links")
    @Valid
    @NotNull(message = "The project links collection is required")
    private List<ProjectLinkDTO> links = new ArrayList<>();

    @JsonProperty("images")
    @Valid
    @NotNull(message = "The project images collection is required")
    @Size(max = 3, message = "A project can have at most 3 images")
    private List<ProjectImageDTO> images = new ArrayList<>();

}
