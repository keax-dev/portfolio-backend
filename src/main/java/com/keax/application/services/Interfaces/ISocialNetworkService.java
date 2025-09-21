package com.keax.application.services.Interfaces;

import com.keax.domain.models.SocialNetwork;
import java.util.List;

public interface ISocialNetworkService {

    SocialNetwork createSocialNetwork(SocialNetwork socialNetwork);
    SocialNetwork updateSocialNetwork(Long socialNetworkId, SocialNetwork socialNetwork);
    SocialNetwork deleteSocialNetwork(Long socialNetworkId);
    List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted);
    List<SocialNetwork> getListSocialNetwork();

}
