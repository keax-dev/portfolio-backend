package com.keax.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Technology {

    @JsonProperty("id")
    private Long technologyId;

    @JsonProperty("name")
    @NotBlank(message = "The technology name is required")
    private String technologyName;

    @JsonProperty("deleted")
    private Boolean technologyDeleted = false;

}
