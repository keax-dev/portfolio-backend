package com.keax.domain.ports.in.SocialNetwork;

import com.keax.domain.models.SocialNetwork;
import java.util.List;

public interface RetrieveSocialNetworkUseCase {

    List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted);
    List<SocialNetwork> getListSocialNetwork();

}
