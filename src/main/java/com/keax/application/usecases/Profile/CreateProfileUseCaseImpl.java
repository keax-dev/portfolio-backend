package com.keax.application.usecases.Profile;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Profile.CreateProfileUseCase;
import com.keax.domain.ports.out.ProfileRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Profile;
import java.util.List;

@Component
public class CreateProfileUseCaseImpl implements CreateProfileUseCase {

    @Autowired
    private ProfileRepositoryPort profileRepositoryPort;

    @Override
    public Profile createProfile(Profile profile) {

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (!profileList.isEmpty()){
            throw new ExceptionAlert("The profile is already created");
        }

        profile.setProfileName(profile.getProfileName().toUpperCase());
        profile.setProfileLastName(profile.getProfileLastName().toUpperCase());
        profile.setProfileTitle(profile.getProfileTitle().toUpperCase());

        return profileRepositoryPort.saveProfile(profile);
    }

}
