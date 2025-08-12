package com.keax.domain.ports.in.Institution;

import com.keax.domain.models.Institution;
import java.util.List;

public interface RetrieveInstitutionUseCase {
    List<Institution> getListInstitution();
    Boolean existsByInstitutionNameIgnoreCase(String institution_name);
}
