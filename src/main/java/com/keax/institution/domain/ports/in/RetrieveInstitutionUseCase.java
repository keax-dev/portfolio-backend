package com.keax.institution.domain.ports.in;

import com.keax.institution.domain.model.Institution;
import java.util.List;

public interface RetrieveInstitutionUseCase {

    List<Institution> getListInstitution();
    List<Institution> findByInstitutionDeleted(Boolean deleted);

}
