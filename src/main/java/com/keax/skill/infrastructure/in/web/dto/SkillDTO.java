package com.keax.skill.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class SkillDTO {

    @JsonProperty("id")
    private Long skillId;

    @JsonProperty("name")
    @NotBlank(message = "The skill name is required")
    @Size(max = 80, message = "The skill name must not exceed 80 characters")
    private String skillName;

    @JsonProperty("picture")
    private String skillPicture;

    @JsonProperty("position")
    @Min(value = 1, message = "The skill position must be greater than 0")
    private int skillPosition;

    @JsonProperty("deleted")
    private Boolean skillDeleted;

}
