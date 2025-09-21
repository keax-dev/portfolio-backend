package com.keax.application.usecases.SocialNetwork;

import com.keax.domain.ports.in.SocialNetwork.DeleteSocialNetworkUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.SocialNetwork;

@Component
public class DeleteSocialNetworkUseCaseImpl implements DeleteSocialNetworkUseCase {

    @Autowired
    private SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public SocialNetwork deleteSocialNetwork(Long socialNetworkId) {

        SocialNetwork socialNetwork = socialNetworkRepositoryPort.findBySocialNetworkIdAndSocialNetworkDeleted(socialNetworkId, false).orElseThrow(
                () -> new ExceptionAlert("The social network entered was not found")
        );

        socialNetwork.setSocialNetworkDeleted(true);

        return socialNetworkRepositoryPort.deleteSocialNetwork(socialNetwork);
    }

}
