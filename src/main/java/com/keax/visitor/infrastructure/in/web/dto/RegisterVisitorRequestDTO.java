package com.keax.visitor.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVisitorRequestDTO {

    @JsonProperty("path")
    @Size(max = 255, message = "The visited path must not exceed 255 characters")
    private String path;

    @JsonProperty("country")
    @Size(max = 120, message = "The visitor country must not exceed 120 characters")
    private String country;

    @JsonProperty("city")
    @Size(max = 120, message = "The visitor city must not exceed 120 characters")
    private String city;

}
