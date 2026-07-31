package com.keax.profile.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.profile.domain.ports.in.UpdateProfileUseCase;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.profile.domain.model.Profile;
import com.keax.shared.domain.text.TextNormalizer;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProfileUseCaseImpl implements UpdateProfileUseCase {
    private final ProfileRepositoryPort profileRepositoryPort;

    @Override
    public Profile updateProfile(Profile profile) {

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ResourceNotFoundException("The profile is not created");
        }

        Profile update = profileList.getFirst();

        update.setProfileTitleEs(TextNormalizer.uppercase(profile.getProfileTitleEs()));
        update.setProfileName(TextNormalizer.uppercase(profile.getProfileName()));
        update.setProfileLastName(TextNormalizer.uppercase(profile.getProfileLastName()));
        update.setProfileTitle(TextNormalizer.uppercase(profile.getProfileTitle()));
        update.setProfileCv(TextNormalizer.trimToNull(profile.getProfileCv()));
        update.setProfileCvEs(TextNormalizer.trimToNull(profile.getProfileCvEs()));

        return profileRepositoryPort.saveProfile(update);
    }

}
