package com.keax.project.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectTechnologyRequestDTO {

    @JsonProperty("id")
    @NotNull(message = "The technology is required")
    private Long technologyId;

    @JsonProperty("position")
    @Min(value = 1, message = "The project technology position must be greater than 0")
    private int position;
}
