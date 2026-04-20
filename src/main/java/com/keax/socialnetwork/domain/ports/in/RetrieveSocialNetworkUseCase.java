package com.keax.socialnetwork.domain.ports.in;

import com.keax.socialnetwork.domain.model.SocialNetwork;
import java.util.List;

public interface RetrieveSocialNetworkUseCase {

    List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted);
    List<SocialNetwork> getListSocialNetwork();

}
