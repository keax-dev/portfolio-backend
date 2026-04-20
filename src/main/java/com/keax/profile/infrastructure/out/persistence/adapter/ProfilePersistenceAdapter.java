package com.keax.profile.infrastructure.out.persistence.adapter;

import com.keax.profile.infrastructure.out.persistence.mapper.ProfilePersistenceMapper;
import com.keax.profile.infrastructure.out.persistence.repository.JpaProfileRepository;
import com.keax.profile.infrastructure.out.persistence.entity.ProfileEntity;
import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.keax.profile.domain.model.Profile;
import java.util.List;

@Repository
public class ProfilePersistenceAdapter implements ProfileRepositoryPort {

    @Autowired
    private JpaProfileRepository jpaProfileRepository;

    @Override
    public Profile saveProfile(Profile profile) {
        ProfileEntity saved = jpaProfileRepository.save(
                ProfilePersistenceMapper.toEntity(profile)
        );
        return ProfilePersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Profile> getListProfile() {
        return jpaProfileRepository.findAll().stream()
                .map(ProfilePersistenceMapper::toDomain)
                .toList();
    }

}
