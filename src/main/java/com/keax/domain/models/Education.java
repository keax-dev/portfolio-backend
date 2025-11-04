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
public class Education {

    @JsonProperty("id")
    private Long educationId;

    @JsonProperty("title")
    @NotBlank(message = "The name of the education is required")
    private String educationTitle;

    @JsonProperty("title_es")
    @NotBlank(message = "The name es of the education is required")
    private String educationTitleEs;

    @JsonProperty("place")
    @NotBlank(message = "The place of education is required")
    private String educationPlace;

    @JsonProperty("start")
    private String educationStart;

    @JsonProperty("start_es")
    private String educationStartEs;

    @JsonProperty("end")
    @NotBlank(message = "The end of education is required")
    private  String educationEnd;

    @JsonProperty("end_es")
    @NotBlank(message = "The end es of education is required")
    private  String educationEndEs;

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
