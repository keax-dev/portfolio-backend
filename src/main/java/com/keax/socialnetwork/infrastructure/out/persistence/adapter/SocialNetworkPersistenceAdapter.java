package com.keax.socialnetwork.infrastructure.out.persistence.adapter;

import com.keax.socialnetwork.infrastructure.out.persistence.mapper.SocialNetworkPersistenceMapper;
import com.keax.socialnetwork.infrastructure.out.persistence.repository.JpaSocialNetworkRepository;
import com.keax.socialnetwork.infrastructure.out.persistence.entity.SocialNetworkEntity;
import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public class SocialNetworkPersistenceAdapter implements SocialNetworkRepositoryPort {

    @Autowired
    private JpaSocialNetworkRepository jpaSocialNetworkRepository;
    @Override
    public SocialNetwork createSocialNetwork(SocialNetwork socialNetwork) {
        SocialNetworkEntity saved = jpaSocialNetworkRepository.save(
                SocialNetworkPersistenceMapper.toEntity(socialNetwork)
        );
        return SocialNetworkPersistenceMapper.toDomain(saved);
    }

    @Override
    public SocialNetwork updateSocialNetwork(SocialNetwork socialNetwork) {
        SocialNetworkEntity updated = jpaSocialNetworkRepository.save(
                SocialNetworkPersistenceMapper.toEntity(socialNetwork)
        );
        return SocialNetworkPersistenceMapper.toDomain(updated);
    }

    @Override
    public SocialNetwork deleteSocialNetwork(SocialNetwork socialNetwork) {
        SocialNetworkEntity deleted = jpaSocialNetworkRepository.save(
                SocialNetworkPersistenceMapper.toEntity(socialNetwork)
        );
        return SocialNetworkPersistenceMapper.toDomain(deleted);
    }

    @Override
    public List<SocialNetwork> findBySocialNetworkDeleted(Boolean deleted) {
        return jpaSocialNetworkRepository.findBySocialNetworkDeleted(deleted)
                .stream()
                .map(SocialNetworkPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<SocialNetwork> getListSocialNetwork() {
        return jpaSocialNetworkRepository.findBySocialNetworkDeleted(false)
                .stream()
                .map(SocialNetworkPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<SocialNetwork> findBySocialNetworkNameAndSocialNetworkDeleted(String socialNetworkName, Boolean deleted) {
        return jpaSocialNetworkRepository.findBySocialNetworkNameAndSocialNetworkDeleted(
                socialNetworkName,
                deleted
        ).map(SocialNetworkPersistenceMapper::toDomain);
    }

    @Override
    public Optional<SocialNetwork> findBySocialNetworkIdAndSocialNetworkDeleted(Long socialNetworkId, Boolean deleted) {
        return jpaSocialNetworkRepository.findBySocialNetworkIdAndSocialNetworkDeleted(
                socialNetworkId,
                deleted
        ).map(SocialNetworkPersistenceMapper::toDomain);
    }

    @Override
    public Optional<SocialNetwork> findBySocialNetworkPositionAndSocialNetworkDeleted(int position, Boolean deleted) {
        return jpaSocialNetworkRepository.findBySocialNetworkPositionAndSocialNetworkDeleted(
                position,
                deleted
        ).map(SocialNetworkPersistenceMapper::toDomain);
    }

}
