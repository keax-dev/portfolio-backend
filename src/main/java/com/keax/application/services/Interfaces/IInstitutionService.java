package com.keax.application.services.Interfaces;

import com.keax.domain.models.Institution;
import java.util.List;

public interface IInstitutionService {
    Institution createInstitution(Institution institution);
    Institution updateInstitution(Long institutionId, Institution institution);
    Boolean deleteInstitution(Long institutionId);
    List<Institution> getListInstitution();
    List<Institution> findByInstitutionDeleted(Boolean deleted);
}
