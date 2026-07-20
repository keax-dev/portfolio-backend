package com.keax.project.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectImageDTO {

    @JsonProperty("id")
    private Long projectImageId;

    @JsonProperty("url")
    @NotBlank(message = "The project image url is required")
    @Size(max = 2048, message = "The project image url must not exceed 2048 characters")
    private String url;

    @JsonProperty("position")
    @Min(value = 1, message = "The project image position must be greater than 0")
    private int position;

}
