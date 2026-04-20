package com.keax.profile.domain.ports.in;

import com.keax.profile.domain.model.Profile;

public interface CreateProfileUseCase {

    Profile createProfile(Profile profile);

}
