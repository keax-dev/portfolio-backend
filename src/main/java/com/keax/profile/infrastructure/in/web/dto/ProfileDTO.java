package com.keax.profile.infrastructure.in.web.dto;

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
public class ProfileDTO {

    @JsonProperty("id")
    private Long profileId;

    @JsonProperty("name")
    @NotBlank(message = "The profile name is required")
    @Size(max = 120, message = "The profile name must not exceed 120 characters")
    private String profileName;

    @JsonProperty("last_name")
    @NotBlank(message = "The profile's last name is required")
    @Size(max = 120, message = "The profile last name must not exceed 120 characters")
    private String profileLastName;

    @JsonProperty("title")
    @NotBlank(message = "The profile title is required")
    @Size(max = 160, message = "The profile title must not exceed 160 characters")
    private String profileTitle;

    @JsonProperty("title_es")
    @NotBlank(message = "The profile title es is required")
    @Size(max = 160, message = "The profile es title must not exceed 160 characters")
    private String profileTitleEs;

    @JsonProperty("cv")
    @Pattern(regexp = "^$|https?://.+", message = "The profile cv url must start with http:// or https://")
    @Size(max = 2048, message = "The profile cv url must not exceed 2048 characters")
    private String profileCv;

    @JsonProperty("image")
    private String profilePicture;

}
