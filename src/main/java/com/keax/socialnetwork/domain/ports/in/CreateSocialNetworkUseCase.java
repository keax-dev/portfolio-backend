package com.keax.socialnetwork.domain.ports.in;

import com.keax.socialnetwork.domain.model.SocialNetwork;

public interface CreateSocialNetworkUseCase {

    SocialNetwork createSocialNetwork(SocialNetwork socialNetwork);

}
