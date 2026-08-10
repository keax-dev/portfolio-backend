package com.keax.experience.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDTO {

    @JsonProperty("id")
    private Long experienceId;

    @JsonProperty("role")
    @NotBlank(message = "The English role is required")
    @Size(max = 160, message = "The English role must not exceed 160 characters")
    private String experienceRole;

    @JsonProperty("role_es")
    @NotBlank(message = "The Spanish role is required")
    @Size(max = 160, message = "The Spanish role must not exceed 160 characters")
    private String experienceRoleEs;

    @JsonProperty("company")
    @NotBlank(message = "The company is required")
    @Size(max = 160, message = "The company must not exceed 160 characters")
    private String experienceCompany;

    @JsonProperty("description")
    @NotBlank(message = "The English description is required")
    @Size(max = 3000, message = "The English description must not exceed 3000 characters")
    private String experienceDescription;

    @JsonProperty("description_es")
    @NotBlank(message = "The Spanish description is required")
    @Size(max = 3000, message = "The Spanish description must not exceed 3000 characters")
    private String experienceDescriptionEs;

    @JsonProperty("start")
    @NotBlank(message = "The English start date is required")
    @Size(max = 80, message = "The English start date must not exceed 80 characters")
    private String experienceStart;

    @JsonProperty("start_es")
    @NotBlank(message = "The Spanish start date is required")
    @Size(max = 80, message = "The Spanish start date must not exceed 80 characters")
    private String experienceStartEs;

    @JsonProperty("end")
    @Size(max = 80, message = "The English end date must not exceed 80 characters")
    private String experienceEnd;

    @JsonProperty("end_es")
    @Size(max = 80, message = "The Spanish end date must not exceed 80 characters")
    private String experienceEndEs;

    @JsonProperty("position")
    @Min(value = 1, message = "The experience position must be greater than 0")
    private int experiencePosition;

    @JsonProperty("visible")
    @NotNull(message = "The experience visibility is required")
    private Boolean experienceVisible = true;
}
