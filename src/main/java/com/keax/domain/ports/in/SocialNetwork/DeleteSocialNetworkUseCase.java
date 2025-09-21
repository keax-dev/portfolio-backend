package com.keax.domain.ports.in.SocialNetwork;

import com.keax.domain.models.SocialNetwork;

public interface DeleteSocialNetworkUseCase {

    SocialNetwork deleteSocialNetwork(Long socialNetworkId);

}
