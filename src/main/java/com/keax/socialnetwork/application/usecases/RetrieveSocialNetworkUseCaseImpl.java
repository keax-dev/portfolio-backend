package com.keax.socialnetwork.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.socialnetwork.domain.ports.in.RetrieveSocialNetworkUseCase;
import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveSocialNetworkUseCaseImpl implements RetrieveSocialNetworkUseCase {
    private final SocialNetworkRepositoryPort socialNetworkRepositoryPort;

    @Override
    public List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted) {

        return socialNetworkRepositoryPort.findBySocialNetworkDeleted(deleted);
    }

    @Override
    public List<SocialNetwork> getListSocialNetwork() {

        return socialNetworkRepositoryPort.getListSocialNetwork();
    }

}
