package com.keax.profile.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.profile.domain.ports.in.CreateProfileUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.profile.domain.model.Profile;
import com.keax.shared.domain.text.TextNormalizer;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProfileUseCaseImpl implements CreateProfileUseCase {
    private final ProfileRepositoryPort profileRepositoryPort;

    @Override
    public Profile createProfile(Profile profile) {

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (!profileList.isEmpty()){
            throw new ResourceConflictException("The profile is already created");
        }

        profile.setProfileTitleEs(TextNormalizer.uppercase(profile.getProfileTitleEs()));
        profile.setProfileName(TextNormalizer.uppercase(profile.getProfileName()));
        profile.setProfileLastName(TextNormalizer.uppercase(profile.getProfileLastName()));
        profile.setProfileTitle(TextNormalizer.uppercase(profile.getProfileTitle()));
        profile.setProfileCv(TextNormalizer.trimToNull(profile.getProfileCv()));
        profile.setProfileCvEs(TextNormalizer.trimToNull(profile.getProfileCvEs()));
        profile.setProfileId(null);

        return profileRepositoryPort.saveProfile(profile);
    }

}
