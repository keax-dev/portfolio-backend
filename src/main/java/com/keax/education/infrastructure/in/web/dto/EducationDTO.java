package com.keax.education.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class EducationDTO {

    @JsonProperty("id")
    private Long educationId;

    @JsonProperty("title")
    @NotBlank(message = "The name of the education is required")
    @Size(max = 160, message = "The education title must not exceed 160 characters")
    private String educationTitle;

    @JsonProperty("title_es")
    @NotBlank(message = "The name es of the education is required")
    @Size(max = 160, message = "The education es title must not exceed 160 characters")
    private String educationTitleEs;

    @JsonProperty("place")
    @NotBlank(message = "The place of education is required")
    @Size(max = 160, message = "The education place must not exceed 160 characters")
    private String educationPlace;

    @JsonProperty("start")
    @Size(max = 80, message = "The education start must not exceed 80 characters")
    private String educationStart;

    @JsonProperty("start_es")
    @Size(max = 80, message = "The education es start must not exceed 80 characters")
    private String educationStartEs;

    @JsonProperty("end")
    @NotBlank(message = "The end of education is required")
    @Size(max = 80, message = "The education end must not exceed 80 characters")
    private String educationEnd;

    @JsonProperty("end_es")
    @NotBlank(message = "The end es of education is required")
    @Size(max = 80, message = "The education es end must not exceed 80 characters")
    private String educationEndEs;

    @JsonProperty("position")
    @Min(value = 1, message = "The education position must be greater than 0")
    private int educationPosition;

    @JsonProperty("deleted")
    private Boolean educationDeleted = false;

    @JsonProperty("institution")
    @NotNull(message = "The institution is required")
    private Long institutionId;

    @JsonProperty("institution_name")
    private String institutionName;

    @JsonProperty("institution_name_es")
    private String institutionNameEs;

    @JsonProperty("institution_url")
    private String institutionUrl;

}
