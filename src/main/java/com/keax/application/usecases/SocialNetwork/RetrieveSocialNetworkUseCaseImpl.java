package com.keax.application.usecases.SocialNetwork;

import com.keax.domain.ports.in.SocialNetwork.RetrieveSocialNetworkUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.SocialNetwork;
import java.util.List;

@Component
public class RetrieveSocialNetworkUseCaseImpl implements RetrieveSocialNetworkUseCase {

    @Autowired
    private SocialNetworkRepositoryPort socialNetworkRepositoryPort;


    @Override
    public List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted) {

        List<SocialNetwork> socialNetworkList = socialNetworkRepositoryPort.findBySocialNetworkDeleted(deleted);

        return validateNotEmpty(socialNetworkList);
    }

    @Override
    public List<SocialNetwork> getListSocialNetwork() {

        List<SocialNetwork> socialNetworkList = socialNetworkRepositoryPort.getListSocialNetwork();

        return validateNotEmpty(socialNetworkList);
    }

    private List<SocialNetwork> validateNotEmpty(List<SocialNetwork> socialNetworks) {

        if (socialNetworks.isEmpty()) {
            throw new ExceptionAlert("Social networks have not been created.");
        }

        return socialNetworks;
    }

}
