package com.keax.domain.ports.out;

import com.keax.domain.models.SocialNetwork;
import java.util.Optional;
import java.util.List;

public interface SocialNetworkRepositoryPort {

    SocialNetwork createSocialNetwork(SocialNetwork socialNetwork);
    SocialNetwork updateSocialNetwork(SocialNetwork socialNetwork);
    SocialNetwork deleteSocialNetwork(SocialNetwork socialNetwork);
    List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted);
    List<SocialNetwork> getListSocialNetwork();
    Optional<SocialNetwork> findBySocialNetworkNameAndSocialNetworkDeleted(String socialNetworkName, Boolean deleted);
    Optional<SocialNetwork> findBySocialNetworkIdAndSocialNetworkDeleted(Long socialNetworkId, Boolean deleted);
    Optional<SocialNetwork> findBySocialNetworkPositionAndSocialNetworkDeleted(int position, Boolean deleted);

}
