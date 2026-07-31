package com.keax.socialnetwork.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.socialnetwork.domain.ports.in.CreateSocialNetworkUseCase;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.text.TextNormalizer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateSocialNetworkUseCaseImpl implements CreateSocialNetworkUseCase {
    private final SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public SocialNetwork createSocialNetwork(SocialNetwork socialNetwork) {

        socialNetwork.setSocialNetworkName(TextNormalizer.uppercase(socialNetwork.getSocialNetworkName()));
        socialNetworkRepositoryPort.findByName(
                socialNetwork.getSocialNetworkName()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a social network with this name");
                }
        );

        socialNetworkRepositoryPort.findByPosition(
                socialNetwork.getSocialNetworkPosition()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("The social network position is already filled");
                }
        );

        socialNetwork.setSocialNetworkIcon(TextNormalizer.trimToNull(socialNetwork.getSocialNetworkIcon()));
        socialNetwork.setSocialNetworkColor(TextNormalizer.trimToNull(socialNetwork.getSocialNetworkColor()));
        socialNetwork.setSocialNetworkUrl(TextNormalizer.trimToNull(socialNetwork.getSocialNetworkUrl()));
        socialNetwork.setSocialNetworkId(null);

        return socialNetworkRepositoryPort.createSocialNetwork(socialNetwork);
    }

}
