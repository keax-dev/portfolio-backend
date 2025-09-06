package com.keax.application.usecases.Profile;

import com.keax.domain.ports.in.Profile.RetrieveProfileUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.ProfileRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Profile;
import java.util.List;

@Component
public class RetrieveProfileUseCaseImpl implements RetrieveProfileUseCase {

    @Autowired
    private ProfileRepositoryPort profileRepositoryPort;

    @Override
    public Profile getProfile() {

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ExceptionAlert("The profile is not created");
        }

        return profileList.getFirst();
    }

}
