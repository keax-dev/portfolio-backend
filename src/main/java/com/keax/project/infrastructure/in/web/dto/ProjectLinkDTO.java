package com.keax.project.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keax.project.domain.model.ProjectLinkType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectLinkDTO {

    @JsonProperty("id")
    private Long projectLinkId;

    @JsonProperty("type")
    @NotNull(message = "The project link type is required")
    private ProjectLinkType type;

    @JsonProperty("url")
    @NotBlank(message = "The project link url is required")
    @Pattern(regexp = "https?://.+", message = "The project link url must start with http:// or https://")
    @Size(max = 2048, message = "The project link url must not exceed 2048 characters")
    private String url;

    @JsonProperty("position")
    @Min(value = 1, message = "The project link position must be greater than 0")
    private int position;
}
