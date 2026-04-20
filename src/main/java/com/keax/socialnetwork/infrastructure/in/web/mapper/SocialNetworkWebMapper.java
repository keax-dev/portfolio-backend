package com.keax.socialnetwork.infrastructure.in.web.mapper;

import com.keax.socialnetwork.infrastructure.in.web.dto.SocialNetworkDTO;
import com.keax.socialnetwork.domain.model.SocialNetwork;

public final class SocialNetworkWebMapper {

    public static SocialNetwork toDomain(SocialNetworkDTO dto) {
        return new SocialNetwork(
                dto.getSocialNetworkId(),
                dto.getSocialNetworkName(),
                dto.getSocialNetworkIcon(),
                dto.getSocialNetworkColor(),
                dto.getSocialNetworkPosition(),
                dto.getSocialNetworkUrl(),
                dto.getSocialNetworkDeleted()
        );
    }

    public static SocialNetworkDTO fromDomain(SocialNetwork socialNetwork) {
        return new SocialNetworkDTO(
                socialNetwork.getSocialNetworkId(),
                socialNetwork.getSocialNetworkName(),
                socialNetwork.getSocialNetworkIcon(),
                socialNetwork.getSocialNetworkColor(),
                socialNetwork.getSocialNetworkPosition(),
                socialNetwork.getSocialNetworkUrl(),
                socialNetwork.getSocialNetworkDeleted()
        );
    }

}
