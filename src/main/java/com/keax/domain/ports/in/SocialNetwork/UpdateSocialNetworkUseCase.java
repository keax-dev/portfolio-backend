package com.keax.domain.ports.in.SocialNetwork;

import com.keax.domain.models.SocialNetwork;

public interface UpdateSocialNetworkUseCase {

    SocialNetwork updateSocialNetwork(Long socialNetworkId, SocialNetwork socialNetwork);

}
