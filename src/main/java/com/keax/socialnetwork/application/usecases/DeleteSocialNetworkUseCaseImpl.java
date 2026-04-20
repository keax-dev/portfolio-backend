package com.keax.socialnetwork.application.usecases;

import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.socialnetwork.domain.ports.in.DeleteSocialNetworkUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeleteSocialNetworkUseCaseImpl implements DeleteSocialNetworkUseCase {

    @Autowired
    private SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public SocialNetwork deleteSocialNetwork(Long socialNetworkId) {

        SocialNetwork socialNetwork = socialNetworkRepositoryPort.findBySocialNetworkIdAndSocialNetworkDeleted(
                socialNetworkId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The social network entered was not found")
        );

        socialNetwork.setSocialNetworkDeleted(true);

        return socialNetworkRepositoryPort.deleteSocialNetwork(socialNetwork);
    }

}
