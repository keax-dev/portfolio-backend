package com.keax.project.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

    @JsonProperty("picture")
    private String projectPicture;

    @JsonProperty("deploy")
    @Pattern(regexp = "^$|https?://.+", message = "The project deploy url must start with http:// or https://")
    @Size(max = 2048, message = "The project deploy url must not exceed 2048 characters")
    private String projectDeploy;

    @JsonProperty("github")
    @Pattern(regexp = "^$|https?://.+", message = "The project github url must start with http:// or https://")
    @Size(max = 2048, message = "The project github url must not exceed 2048 characters")
    private String projectGithub;

    @JsonProperty("position")
    @Min(value = 1, message = "The project position must be greater than 0")
    private int projectPosition;

    @JsonProperty("technology")
    @NotNull(message = "The technology is required")
    private Long technologyId;

    @JsonProperty("technology_name")
    private String technologyName;

    @JsonProperty("deleted")
    private Boolean projectDeleted;

}
