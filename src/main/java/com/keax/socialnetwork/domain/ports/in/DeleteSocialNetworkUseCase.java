package com.keax.socialnetwork.domain.ports.in;

import com.keax.socialnetwork.domain.model.SocialNetwork;

public interface DeleteSocialNetworkUseCase {

    SocialNetwork deleteSocialNetwork(Long socialNetworkId);

}
