package com.keax.socialnetwork.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.socialnetwork.domain.ports.in.UpdateSocialNetworkUseCase;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.domain.text.TextNormalizer;
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

        SocialNetwork socialNetworkUpdate = socialNetworkRepositoryPort.findById(socialNetworkId).orElseThrow(
                () -> new ResourceNotFoundException("The social network entered was not found")
        );

        socialNetworkUpdate.setSocialNetworkName(TextNormalizer.uppercase(socialNetwork.getSocialNetworkName()));
        socialNetworkRepositoryPort.findByName(
                socialNetworkUpdate.getSocialNetworkName()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getSocialNetworkId(), socialNetworkUpdate.getSocialNetworkId())){
                        throw new ResourceConflictException("The name of the social network to be updated is already registered");
                    }
                }
        );

        socialNetworkRepositoryPort.findByPosition(
                socialNetwork.getSocialNetworkPosition()
        ).ifPresent(
                e -> {
                    if (!Objects.equals(e.getSocialNetworkId(), socialNetworkUpdate.getSocialNetworkId())){
                        throw new ResourceConflictException("The social network position is already filled");
                    }
                }
        );

        socialNetworkUpdate.setSocialNetworkIcon(TextNormalizer.trimToNull(socialNetwork.getSocialNetworkIcon()));
        socialNetworkUpdate.setSocialNetworkColor(TextNormalizer.trimToNull(socialNetwork.getSocialNetworkColor()));
        socialNetworkUpdate.setSocialNetworkPosition(socialNetwork.getSocialNetworkPosition());
        socialNetworkUpdate.setSocialNetworkUrl(TextNormalizer.trimToNull(socialNetwork.getSocialNetworkUrl()));

        return socialNetworkRepositoryPort.updateSocialNetwork(socialNetworkUpdate);
    }

}
