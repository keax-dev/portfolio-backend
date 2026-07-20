package com.keax.project.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public abstract class ProjectWriteDTO {

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

    @JsonProperty("technologies")
    @Valid
    @NotEmpty(message = "At least one project technology is required")
    @Size(max = 20, message = "A project can have at most 20 technologies")
    private List<ProjectTechnologyRequestDTO> technologies = new ArrayList<>();

    @JsonProperty("links")
    @Valid
    @NotNull(message = "The project links collection is required")
    @Size(max = 10, message = "A project can have at most 10 links")
    private List<ProjectLinkRequestDTO> links = new ArrayList<>();
}
