package com.keax.socialnetwork.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;

import com.keax.socialnetwork.infrastructure.out.persistence.mapper.SocialNetworkPersistenceMapper;
import com.keax.socialnetwork.infrastructure.out.persistence.repository.JpaSocialNetworkRepository;
import com.keax.socialnetwork.infrastructure.out.persistence.entity.SocialNetworkEntity;
import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SocialNetworkPersistenceAdapter implements SocialNetworkRepositoryPort {
    private final JpaSocialNetworkRepository jpaSocialNetworkRepository;
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
        jpaSocialNetworkRepository.deleteById(socialNetwork.getSocialNetworkId());
        jpaSocialNetworkRepository.flush();
        return socialNetwork;
    }

    @Override
    public List<SocialNetwork> findAll() {
        return jpaSocialNetworkRepository.findAllByOrderBySocialNetworkPositionAsc()
                .stream()
                .map(SocialNetworkPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<SocialNetwork> findByName(String socialNetworkName) {
        return jpaSocialNetworkRepository.findBySocialNetworkName(socialNetworkName)
                .map(SocialNetworkPersistenceMapper::toDomain);
    }

    @Override
    public Optional<SocialNetwork> findById(Long socialNetworkId) {
        return jpaSocialNetworkRepository.findById(socialNetworkId)
                .map(SocialNetworkPersistenceMapper::toDomain);
    }

    @Override
    public Optional<SocialNetwork> findByPosition(int position) {
        return jpaSocialNetworkRepository.findBySocialNetworkPosition(position)
                .map(SocialNetworkPersistenceMapper::toDomain);
    }

}
