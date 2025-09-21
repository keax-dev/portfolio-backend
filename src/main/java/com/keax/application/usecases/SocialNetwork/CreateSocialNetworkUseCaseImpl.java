package com.keax.application.usecases.SocialNetwork;

import com.keax.domain.ports.in.SocialNetwork.CreateSocialNetworkUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.SocialNetwork;

@Component
public class CreateSocialNetworkUseCaseImpl implements CreateSocialNetworkUseCase {

    @Autowired
    private SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public SocialNetwork createSocialNetwork(SocialNetwork socialNetwork) {

        socialNetwork.setSocialNetworkName(socialNetwork.getSocialNetworkName().toUpperCase());

        socialNetworkRepositoryPort.findBySocialNetworkNameAndSocialNetworkDeleted(socialNetwork.getSocialNetworkName(), false).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already a social network with this name");
                }
        );

        socialNetworkRepositoryPort.findBySocialNetworkPositionAndSocialNetworkDeleted(socialNetwork.getSocialNetworkPosition(), false).ifPresent(
                e -> {
                    throw new ExceptionAlert("The social network position is already filled");
                }
        );

        socialNetwork.setSocialNetworkId(null);

        return socialNetworkRepositoryPort.createSocialNetwork(socialNetwork);
    }

}
