package com.keax.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialNetwork {

    @JsonProperty("id")
    private Long socialNetworkId;

    @JsonProperty("name")
    @NotBlank(message = "The social network name is required")
    private String socialNetworkName;

    @JsonProperty("icon")
    @NotBlank(message = "The social network icon is required")
    private String socialNetworkIcon;

    @JsonProperty("color")
    @NotBlank(message = "The social network color is required")
    private String socialNetworkColor;

    @JsonProperty("position")
    @Min(value = 1, message = "The social network position must be greater than 0")
    private int socialNetworkPosition;

    @JsonProperty("url")
    @NotBlank(message = "The social network url is required")
    private String socialNetworkUrl;

    @JsonProperty("deleted")
    private Boolean socialNetworkDeleted = false;

}
