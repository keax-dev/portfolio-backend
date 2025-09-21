package com.keax.infrastructure.adapters;

import com.keax.infrastructure.repositories.JpaSocialNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.infrastructure.entities.SocialNetworkEntity;
import org.springframework.stereotype.Repository;
import com.keax.domain.models.SocialNetwork;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@Repository
public class JpaSocialNetworkRepositoryAdapter implements SocialNetworkRepositoryPort {

    @Autowired
    private JpaSocialNetworkRepository jpaSocialNetworkRepository;

    @Override
    public SocialNetwork createSocialNetwork(SocialNetwork socialNetwork) {
        SocialNetworkEntity saved = jpaSocialNetworkRepository.save(fromDomainModel(socialNetwork));
        return toDomainModel(saved);
    }

    @Override
    public SocialNetwork updateSocialNetwork(SocialNetwork socialNetwork) {
        SocialNetworkEntity update = jpaSocialNetworkRepository.save(fromDomainModel(socialNetwork));
        return toDomainModel(update);
    }

    @Override
    public SocialNetwork deleteSocialNetwork(SocialNetwork socialNetwork) {
        SocialNetworkEntity deleted = jpaSocialNetworkRepository.save(fromDomainModel(socialNetwork));
        return toDomainModel(deleted);
    }

    @Override
    public List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted) {
        return jpaSocialNetworkRepository.findBySocialNetworkDeleted(deleted).stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public List<SocialNetwork> getListSocialNetwork() {
        return jpaSocialNetworkRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Optional<SocialNetwork> findBySocialNetworkNameAndSocialNetworkDeleted(String socialNetworkName, Boolean deleted) {
        return jpaSocialNetworkRepository.findBySocialNetworkNameAndSocialNetworkDeleted(socialNetworkName, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<SocialNetwork> findBySocialNetworkIdAndSocialNetworkDeleted(Long socialNetworkId, Boolean deleted) {
        return jpaSocialNetworkRepository.findBySocialNetworkIdAndSocialNetworkDeleted(socialNetworkId, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<SocialNetwork> findBySocialNetworkPositionAndSocialNetworkDeleted(int position, Boolean deleted) {
        return jpaSocialNetworkRepository.findBySocialNetworkPositionAndSocialNetworkDeleted(position, deleted).map(this::toDomainModel);
    }

    private SocialNetwork toDomainModel(SocialNetworkEntity socialNetworkEntity){
        return new SocialNetwork(
                socialNetworkEntity.getSocialNetworkId(),
                socialNetworkEntity.getSocialNetworkName(),
                socialNetworkEntity.getSocialNetworkIcon(),
                socialNetworkEntity.getSocialNetworkColor(),
                socialNetworkEntity.getSocialNetworkPosition(),
                socialNetworkEntity.getSocialNetworkUrl(),
                socialNetworkEntity.getSocialNetworkDeleted()
        );
    }

    private SocialNetworkEntity fromDomainModel(SocialNetwork socialNetwork){
        return new SocialNetworkEntity(
                socialNetwork.getSocialNetworkId(),
                socialNetwork.getSocialNetworkName(),
                socialNetwork.getSocialNetworkIcon(),
                socialNetwork.getSocialNetworkColor(),
                socialNetwork.getSocialNetworkPosition(),
                socialNetwork.getSocialNetworkUrl(),
                socialNetwork.getSocialNetworkDeleted()
        );
    }

}
