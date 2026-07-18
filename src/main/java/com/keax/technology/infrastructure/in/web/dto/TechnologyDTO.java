package com.keax.technology.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class TechnologyDTO {

    @JsonProperty("id")
    private Long technologyId;

    @JsonProperty("name")
    @NotBlank(message = "The technology name is required")
    @Size(max = 80, message = "The technology name must not exceed 80 characters")
    private String technologyName;

    @JsonProperty("position")
    @Min(value = 1, message = "The technology position must be greater than 0")
    private int technologyPosition;

    @JsonProperty("deleted")
    private Boolean technologyDeleted;

}
