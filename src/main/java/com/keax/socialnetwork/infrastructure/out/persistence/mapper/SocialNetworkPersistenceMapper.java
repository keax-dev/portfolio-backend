package com.keax.socialnetwork.infrastructure.out.persistence.mapper;

import com.keax.socialnetwork.infrastructure.out.persistence.entity.SocialNetworkEntity;
import com.keax.socialnetwork.domain.model.SocialNetwork;

public final class SocialNetworkPersistenceMapper {

    public static SocialNetwork toDomain(SocialNetworkEntity entity) {
        return new SocialNetwork(
                entity.getSocialNetworkId(),
                entity.getSocialNetworkName(),
                entity.getSocialNetworkIcon(),
                entity.getSocialNetworkColor(),
                entity.getSocialNetworkPosition(),
                entity.getSocialNetworkUrl(),
                entity.getSocialNetworkDeleted()
        );
    }

    public static SocialNetworkEntity toEntity(SocialNetwork socialNetwork) {
        return new SocialNetworkEntity(
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
