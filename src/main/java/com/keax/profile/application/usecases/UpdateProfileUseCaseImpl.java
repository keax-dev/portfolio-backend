package com.keax.profile.application.usecases;

import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.profile.domain.ports.in.UpdateProfileUseCase;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.profile.domain.model.Profile;
import java.util.List;

@Service
@Transactional
public class UpdateProfileUseCaseImpl implements UpdateProfileUseCase {

    @Autowired
    private ProfileRepositoryPort profileRepositoryPort;

    @Override
    public Profile updateProfile(Profile profile) {

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ResourceNotFoundException("The profile is not created");
        }

        Profile update = profileList.getFirst();

        update.setProfileTitleEs(profile.getProfileTitleEs().toUpperCase());
        update.setProfileName(profile.getProfileName().toUpperCase());
        update.setProfileLastName(profile.getProfileLastName().toUpperCase());
        update.setProfileTitle(profile.getProfileTitle().toUpperCase());
        update.setProfileCv(profile.getProfileCv());

        return profileRepositoryPort.saveProfile(update);
    }

}
