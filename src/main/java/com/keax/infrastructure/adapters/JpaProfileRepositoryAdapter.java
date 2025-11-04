package com.keax.infrastructure.adapters;

import com.keax.infrastructure.repositories.JpaProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.ProfileRepositoryPort;
import com.keax.infrastructure.entities.ProfileEntity;
import org.springframework.stereotype.Repository;
import com.keax.domain.models.Profile;
import java.util.stream.Collectors;
import java.util.List;

@Repository
public class JpaProfileRepositoryAdapter implements ProfileRepositoryPort {

    @Autowired
    private JpaProfileRepository jpaProfileRepository;

    @Override
    public Profile saveProfile(Profile profile) {
        ProfileEntity saved = jpaProfileRepository.save(fromDomainModel(profile));
        return toDomainModel(saved);
    }

    @Override
    public List<Profile> getListProfile() {
        return jpaProfileRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    private Profile toDomainModel(ProfileEntity profileEntity){
        return new Profile(
                profileEntity.getProfileId(),
                profileEntity.getProfileName(),
                profileEntity.getProfileLastName(),
                profileEntity.getProfileTitle(),
                profileEntity.getProfileTitleEs(),
                profileEntity.getProfileCv(),
                profileEntity.getProfilePicture()
        );
    }

    private ProfileEntity fromDomainModel(Profile profile){
        return new ProfileEntity(
                profile.getProfileId(),
                profile.getProfileName(),
                profile.getProfileLastName(),
                profile.getProfileTitle(),
                profile.getProfileTitleEs(),
                profile.getProfileCv(),
                profile.getProfilePicture()
        );
    }

}
