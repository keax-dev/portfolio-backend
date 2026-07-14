package com.keax.socialnetwork.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.socialnetwork.domain.ports.in.UpdateSocialNetworkUseCase;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateSocialNetworkUseCaseImpl implements UpdateSocialNetworkUseCase {
    private final SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public SocialNetwork updateSocialNetwork(Long socialNetworkId, SocialNetwork socialNetwork) {

        SocialNetwork socialNetworkUpdate = socialNetworkRepositoryPort.findBySocialNetworkIdAndSocialNetworkDeleted(
                socialNetworkId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The social network entered was not found")
        );

        socialNetworkUpdate.setSocialNetworkName(socialNetwork.getSocialNetworkName().toUpperCase());
        socialNetworkRepositoryPort.findBySocialNetworkNameAndSocialNetworkDeleted(
                socialNetworkUpdate.getSocialNetworkName(),
                false
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getSocialNetworkId(), socialNetworkUpdate.getSocialNetworkId())){
                        throw new ResourceConflictException("The name of the social network to be updated is already registered");
                    }
                }
        );

        socialNetworkRepositoryPort.findBySocialNetworkPositionAndSocialNetworkDeleted(
                socialNetwork.getSocialNetworkPosition(),
                false
        ).ifPresent(
                e -> {
                    if (!Objects.equals(e.getSocialNetworkId(), socialNetworkUpdate.getSocialNetworkId())){
                        throw new ResourceConflictException("The social network position is already filled");
                    }
                }
        );

        socialNetworkUpdate.setSocialNetworkIcon(socialNetwork.getSocialNetworkIcon());
        socialNetworkUpdate.setSocialNetworkColor(socialNetwork.getSocialNetworkColor());
        socialNetworkUpdate.setSocialNetworkPosition(socialNetwork.getSocialNetworkPosition());
        socialNetworkUpdate.setSocialNetworkUrl(socialNetwork.getSocialNetworkUrl());
        socialNetworkUpdate.setSocialNetworkDeleted(false);

        return socialNetworkRepositoryPort.updateSocialNetwork(socialNetworkUpdate);
    }

}
