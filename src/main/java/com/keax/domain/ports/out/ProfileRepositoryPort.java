package com.keax.domain.ports.out;

import com.keax.domain.models.Profile;
import java.util.List;

public interface ProfileRepositoryPort {

    Profile saveProfile(Profile profile);
    List<Profile> getListProfile();

}
