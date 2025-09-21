package com.keax.application.services.Implementation;

import com.keax.domain.ports.in.SocialNetwork.RetrieveSocialNetworkUseCase;
import com.keax.domain.ports.in.SocialNetwork.UpdateSocialNetworkUseCase;
import com.keax.domain.ports.in.SocialNetwork.CreateSocialNetworkUseCase;
import com.keax.domain.ports.in.SocialNetwork.DeleteSocialNetworkUseCase;
import com.keax.application.services.Interfaces.ISocialNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keax.domain.models.SocialNetwork;
import java.util.List;

@Service
public class SocialNetworkService implements ISocialNetworkService {

    @Autowired
    private CreateSocialNetworkUseCase createSocialNetworkUseCase;

    @Autowired
    private UpdateSocialNetworkUseCase updateSocialNetworkUseCase;

    @Autowired
    private DeleteSocialNetworkUseCase deleteSocialNetworkUseCase;

    @Autowired
    private RetrieveSocialNetworkUseCase retrieveSocialNetworkUseCase;

    @Override
    public SocialNetwork createSocialNetwork(SocialNetwork socialNetwork) {
        return createSocialNetworkUseCase.createSocialNetwork(socialNetwork);
    }

    @Override
    public SocialNetwork updateSocialNetwork(Long socialNetworkId, SocialNetwork socialNetwork) {
        return updateSocialNetworkUseCase.updateSocialNetwork(socialNetworkId, socialNetwork);
    }

    @Override
    public SocialNetwork deleteSocialNetwork(Long socialNetworkId) {
        return deleteSocialNetworkUseCase.deleteSocialNetwork(socialNetworkId);
    }

    @Override
    public List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted) {
        return retrieveSocialNetworkUseCase.findBySocialNetworkDeleted(deleted);
    }

    @Override
    public List<SocialNetwork> getListSocialNetwork() {
        return retrieveSocialNetworkUseCase.getListSocialNetwork();
    }
}
