package com.keax.application.services.Implementation;

import com.keax.application.services.Interfaces.IProfileService;
import com.keax.domain.ports.in.Profile.RetrieveProfileUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Profile.CreateProfileUseCase;
import com.keax.domain.ports.in.Profile.UpdateProfileUseCase;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Profile;

@Service
public class ProfileServiceImpl implements IProfileService {

    @Autowired
    private CreateProfileUseCase createProfileUseCase;

    @Autowired
    private UpdateProfileUseCase updateProfileUseCase;

    @Autowired
    private RetrieveProfileUseCase retrieveProfileUseCase;

    @Override
    public Profile createProfile(Profile profile) {
        return createProfileUseCase.createProfile(profile);
    }

    @Override
    public Profile updateProfile(Profile profile) {
        return updateProfileUseCase.updateProfile(profile);
    }

    @Override
    public Profile getProfile() {
        return retrieveProfileUseCase.getProfile();
    }

}
