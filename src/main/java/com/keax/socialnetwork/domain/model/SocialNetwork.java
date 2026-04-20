package com.keax.socialnetwork.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialNetwork {

    private Long socialNetworkId;
    private String socialNetworkName;
    private String socialNetworkIcon;
    private String socialNetworkColor;
    private int socialNetworkPosition;
    private String socialNetworkUrl;
    private Boolean socialNetworkDeleted;

}
