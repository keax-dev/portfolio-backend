package com.keax.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Technology {

    @JsonProperty("id")
    private Long technologyId;

    @JsonProperty("name")
    @NotBlank(message = "The technology name is required")
    private String technologyName;

    @JsonProperty("position")
    @Min(value = 1, message = "The technology position must be greater than 0")
    private int technologyPosition;

    @JsonProperty("deleted")
    private Boolean technologyDeleted = false;

    @JsonProperty("projects")
    private List<Project> projectList = new ArrayList<>();

}
