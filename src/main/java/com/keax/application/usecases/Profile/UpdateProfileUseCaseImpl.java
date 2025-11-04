package com.keax.application.usecases.Profile;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Profile.UpdateProfileUseCase;
import com.keax.domain.ports.out.ProfileRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Profile;
import java.util.List;

@Component
public class UpdateProfileUseCaseImpl implements UpdateProfileUseCase {

    @Autowired
    private ProfileRepositoryPort profileRepositoryPort;

    @Override
    public Profile updateProfile(Profile profile) {

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ExceptionAlert("The profile is not created");
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
