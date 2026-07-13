package com.keax.socialnetwork.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.socialnetwork.domain.ports.in.RetrieveSocialNetworkUseCase;
import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveSocialNetworkUseCaseImpl implements RetrieveSocialNetworkUseCase {
    private final SocialNetworkRepositoryPort socialNetworkRepositoryPort;

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
