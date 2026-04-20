package com.keax.profile.infrastructure.out.persistence.mapper;

import com.keax.profile.infrastructure.out.persistence.entity.ProfileEntity;
import com.keax.profile.domain.model.Profile;

public final class ProfilePersistenceMapper {

    public static Profile toDomain(ProfileEntity entity) {
        return new Profile(
                entity.getProfileId(),
                entity.getProfileName(),
                entity.getProfileLastName(),
                entity.getProfileTitle(),
                entity.getProfileTitleEs(),
                entity.getProfileCv(),
                entity.getProfilePicture()
        );
    }

    public static ProfileEntity toEntity(Profile profile) {
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
