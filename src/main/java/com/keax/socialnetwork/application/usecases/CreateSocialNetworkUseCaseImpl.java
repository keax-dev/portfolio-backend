package com.keax.socialnetwork.application.usecases;

import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.socialnetwork.domain.ports.in.CreateSocialNetworkUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CreateSocialNetworkUseCaseImpl implements CreateSocialNetworkUseCase {

    @Autowired
    private SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public SocialNetwork createSocialNetwork(SocialNetwork socialNetwork) {

        socialNetwork.setSocialNetworkName(socialNetwork.getSocialNetworkName().toUpperCase());
        socialNetworkRepositoryPort.findBySocialNetworkNameAndSocialNetworkDeleted(
                socialNetwork.getSocialNetworkName(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a social network with this name");
                }
        );

        socialNetworkRepositoryPort.findBySocialNetworkPositionAndSocialNetworkDeleted(
                socialNetwork.getSocialNetworkPosition(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("The social network position is already filled");
                }
        );

        socialNetwork.setSocialNetworkId(null);
        socialNetwork.setSocialNetworkDeleted(false);

        return socialNetworkRepositoryPort.createSocialNetwork(socialNetwork);
    }

}
