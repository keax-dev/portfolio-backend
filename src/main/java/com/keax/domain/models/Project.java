package com.keax.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @JsonProperty("id")
    private Long projectId;

    @JsonProperty("tittle")
    @NotBlank(message = "The project tittle is required")
    private String projectTittle;

    @JsonProperty("description")
    @NotBlank(message = "The project description is required")
    private String projectDescription;

    @JsonProperty("picture")
    private String projectPicture;

    @JsonProperty("deploy")
    private String projectDeploy;

    @JsonProperty("github")
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
    private Boolean projectDeleted = false;

}
