package com.keax.socialnetwork.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class SocialNetworkDTO {

    @JsonProperty("id")
    private Long socialNetworkId;

    @JsonProperty("name")
    @NotBlank(message = "The social network name is required")
    @Size(max = 80, message = "The social network name must not exceed 80 characters")
    private String socialNetworkName;

    @JsonProperty("icon")
    @NotBlank(message = "The social network icon is required")
    @Size(max = 120, message = "The social network icon must not exceed 120 characters")
    private String socialNetworkIcon;

    @JsonProperty("color")
    @NotBlank(message = "The social network color is required")
    @Size(max = 40, message = "The social network color must not exceed 40 characters")
    private String socialNetworkColor;

    @JsonProperty("position")
    @Min(value = 1, message = "The social network position must be greater than 0")
    private int socialNetworkPosition;

    @JsonProperty("url")
    @NotBlank(message = "The social network url is required")
    @Pattern(regexp = "https?://.+", message = "The social network url must start with http:// or https://")
    @Size(max = 2048, message = "The social network url must not exceed 2048 characters")
    private String socialNetworkUrl;

    @JsonProperty("deleted")
    private Boolean socialNetworkDeleted;

}
