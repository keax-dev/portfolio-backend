package com.keax.profile.domain.ports.out;

import com.keax.profile.domain.model.Profile;
import java.util.List;

public interface ProfileRepositoryPort {

    Profile saveProfile(Profile profile);
    List<Profile> getListProfile();

}
