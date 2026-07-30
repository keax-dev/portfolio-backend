package com.keax.course.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class CourseDTO {

    @JsonProperty("id")
    private Long courseId;

    @JsonProperty("name")
    @NotBlank(message = "The course name is required")
    @Size(max = 200, message = "The course name must not exceed 200 characters")
    private String courseName;

    @JsonProperty("name_en")
    @NotBlank(message = "The English course name is required")
    @Size(max = 200, message = "The English course name must not exceed 200 characters")
    private String courseNameEn;

    @JsonProperty("certificate_img")
    @Size(max = 2048, message = "The certificate image URL must not exceed 2048 characters")
    private String courseCertificateImg;

    @JsonProperty("certificate_url")
    @Size(max = 2048, message = "The certificate URL must not exceed 2048 characters")
    @Pattern(
            regexp = "^$|https?://.+",
            message = "The certificate URL must start with http:// or https://"
    )
    private String courseCertificateUrl;

    @JsonProperty("position")
    @NotNull(message = "The course position is required")
    @Min(value = 1, message = "The course position must be greater than 0")
    private Integer coursePosition;

    @JsonProperty(value = "deleted", access = JsonProperty.Access.READ_ONLY)
    private Boolean courseDeleted = false;

    @JsonProperty("institution")
    @NotNull(message = "The institution is required")
    private Long institutionId;

    @JsonProperty("institution_name")
    private String institutionName;

    @JsonProperty("institution_name_es")
    private String institutionNameEs;
}
