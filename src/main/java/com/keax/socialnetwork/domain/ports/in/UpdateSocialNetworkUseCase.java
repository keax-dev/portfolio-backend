package com.keax.socialnetwork.domain.ports.in;

import com.keax.socialnetwork.domain.model.SocialNetwork;

public interface UpdateSocialNetworkUseCase {

    SocialNetwork updateSocialNetwork(Long socialNetworkId, SocialNetwork socialNetwork);

}
