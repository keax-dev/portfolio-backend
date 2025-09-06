package com.keax.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @JsonProperty("id")
    private Long profileId;

    @JsonProperty("name")
    @NotBlank(message = "The profile name is required")
    private String profileName;

    @JsonProperty("last_name")
    @NotBlank(message = "The profile's last name is required")
    private String profileLastName;

    @JsonProperty("title")
    @NotBlank(message = "The profile title is required")
    private String profileTitle;

    @Column(name = "profile_cv")
    private String profileCv;

    @Column(name = "profile_picture")
    private String profilePicture;

}
