package com.keax.project.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTechnologyDTO {

    @JsonProperty("relation_id")
    private Long projectTechnologyId;

    @JsonProperty("id")
    @NotNull(message = "The technology is required")
    private Long technologyId;

    @JsonProperty("name")
    private String technologyName;

    @JsonProperty("position")
    @Min(value = 1, message = "The project technology position must be greater than 0")
    private int position;
}
