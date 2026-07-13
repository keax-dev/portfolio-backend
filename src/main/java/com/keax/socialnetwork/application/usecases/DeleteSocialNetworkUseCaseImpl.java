package com.keax.socialnetwork.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.socialnetwork.domain.ports.in.DeleteSocialNetworkUseCase;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteSocialNetworkUseCaseImpl implements DeleteSocialNetworkUseCase {
    private final SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public SocialNetwork deleteSocialNetwork(Long socialNetworkId) {

        SocialNetwork socialNetwork = socialNetworkRepositoryPort.findBySocialNetworkIdAndSocialNetworkDeleted(
                socialNetworkId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The social network entered was not found")
        );

        socialNetwork.setSocialNetworkDeleted(true);

        return socialNetworkRepositoryPort.deleteSocialNetwork(socialNetwork);
    }

}
