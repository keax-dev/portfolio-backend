package com.keax.profile.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.profile.domain.ports.in.RetrieveProfileUseCase;
import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.keax.profile.domain.model.Profile;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveProfileUseCaseImpl implements RetrieveProfileUseCase {
    private final ProfileRepositoryPort profileRepositoryPort;

    @Override
    public Profile getProfile() {

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ResourceNotFoundException("The profile is not created");
        }

        return profileList.getFirst();
    }

}
