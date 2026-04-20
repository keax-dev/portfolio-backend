package com.keax.institution.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionDTO {

    @JsonProperty("id")
    private Long institutionId;

    @JsonProperty("name")
    @NotBlank(message = "The name of the institution is required")
    @Size(max = 160, message = "The institution name must not exceed 160 characters")
    private String institutionName;

    @JsonProperty("name_es")
    @NotBlank(message = "The name es of the institution is required")
    @Size(max = 160, message = "The institution es name must not exceed 160 characters")
    private String institutionNameEs;

    @JsonProperty("url")
    @Pattern(regexp = "^$|https?://.+", message = "The institution url must start with http:// or https://")
    @Size(max = 2048, message = "The institution url must not exceed 2048 characters")
    private String institutionUrl;

    @JsonProperty("deleted")
    private Boolean institutionDeleted;

}
