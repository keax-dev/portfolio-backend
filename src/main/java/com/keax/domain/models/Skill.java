package com.keax.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @JsonProperty("id")
    private Long skillId;

    @JsonProperty("name")
    @NotBlank(message = "The skill name is required")
    private String skillName;

    @JsonProperty("picture")
    private String skillPicture;

    @JsonProperty("deleted")
    private Boolean skillDeleted = false;

}
