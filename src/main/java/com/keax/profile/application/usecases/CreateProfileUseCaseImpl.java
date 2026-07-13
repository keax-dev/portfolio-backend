package com.keax.profile.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.profile.domain.ports.in.CreateProfileUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.profile.domain.model.Profile;
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

        profile.setProfileTitleEs(profile.getProfileTitleEs().toUpperCase());
        profile.setProfileName(profile.getProfileName().toUpperCase());
        profile.setProfileLastName(profile.getProfileLastName().toUpperCase());
        profile.setProfileTitle(profile.getProfileTitle().toUpperCase());
        profile.setProfileId(null);

        return profileRepositoryPort.saveProfile(profile);
    }

}
