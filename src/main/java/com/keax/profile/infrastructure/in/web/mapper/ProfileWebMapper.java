package com.keax.profile.infrastructure.in.web.mapper;

import com.keax.profile.infrastructure.in.web.dto.ProfileDTO;
import com.keax.profile.domain.model.Profile;

public final class ProfileWebMapper {

    public static Profile toDomain(ProfileDTO dto) {
        return new Profile(
                dto.getProfileId(),
                dto.getProfileName(),
                dto.getProfileLastName(),
                dto.getProfileTitle(),
                dto.getProfileTitleEs(),
                dto.getProfileCv(),
                dto.getProfilePicture()
        );
    }

    public static ProfileDTO fromDomain(Profile profile) {
        return new ProfileDTO(
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
