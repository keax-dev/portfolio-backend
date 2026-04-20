package com.keax.socialnetwork.application.usecases;

import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.socialnetwork.domain.ports.in.UpdateSocialNetworkUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
public class UpdateSocialNetworkUseCaseImpl implements UpdateSocialNetworkUseCase {

    @Autowired
    private SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public SocialNetwork updateSocialNetwork(Long socialNetworkId, SocialNetwork socialNetwork) {

        SocialNetwork socialNetworkUpdate = socialNetworkRepositoryPort.findBySocialNetworkIdAndSocialNetworkDeleted(
                socialNetworkId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The social network entered was not found")
        );

        socialNetworkUpdate.setSocialNetworkName(socialNetwork.getSocialNetworkName().toUpperCase());
        socialNetworkRepositoryPort.findBySocialNetworkNameAndSocialNetworkDeleted(
                socialNetworkUpdate.getSocialNetworkName(),
                false
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getSocialNetworkId(), socialNetworkUpdate.getSocialNetworkId())){
                        throw new ExceptionAlert("The name of the social network to be updated is already registered");
                    }
                }
        );

        socialNetworkRepositoryPort.findBySocialNetworkPositionAndSocialNetworkDeleted(
                socialNetwork.getSocialNetworkPosition(),
                false
        ).ifPresent(
                e -> {
                    if (!Objects.equals(e.getSocialNetworkId(), socialNetworkUpdate.getSocialNetworkId())){
                        throw new ExceptionAlert("The social network position is already filled");
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
