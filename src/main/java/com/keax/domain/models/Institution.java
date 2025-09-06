package com.keax.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Institution {

    @JsonProperty("id")
    private Long institutionId;

    @JsonProperty("name")
    @NotBlank(message = "The name of the institution is required")
    private String institutionName;

    @JsonProperty("url")
    private String institutionUrl;

    @JsonProperty("deleted")
    private Boolean institutionDeleted = false;

}
