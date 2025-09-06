package com.keax.application.services.Interfaces;

import com.keax.domain.models.Profile;

public interface IProfileService {

    Profile createProfile(Profile profile);
    Profile updateProfile(Profile profile);
    Profile getProfile();

}
