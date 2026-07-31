package com.keax.socialnetwork.domain.ports.out;

import com.keax.socialnetwork.domain.model.SocialNetwork;
import java.util.Optional;
import java.util.List;

public interface SocialNetworkRepositoryPort {

    SocialNetwork createSocialNetwork(SocialNetwork socialNetwork);
    SocialNetwork updateSocialNetwork(SocialNetwork socialNetwork);
    SocialNetwork deleteSocialNetwork(SocialNetwork socialNetwork);
    List<SocialNetwork> findAll();
    Optional<SocialNetwork> findByName(String socialNetworkName);
    Optional<SocialNetwork> findById(Long socialNetworkId);
    Optional<SocialNetwork> findByPosition(int position);

}
