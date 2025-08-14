package com.keax.application.services.Interfaces;

import com.keax.domain.models.Institution;

import java.util.List;
import java.util.Optional;

public interface IInstitutionService {
    Institution createInstitution(Institution institution);
    Institution updateInstitution(Long institution_id, Institution institution);
    Boolean deleteInstitution(Long institution_id);
    List<Institution> getListInstitution();
    List<Institution> findByInstitutionDeleted(Boolean deleted);
}
