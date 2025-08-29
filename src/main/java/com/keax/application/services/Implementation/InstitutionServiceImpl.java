package com.keax.application.services.Implementation;

import com.keax.domain.ports.in.Institution.RetrieveInstitutionUseCase;
import com.keax.domain.ports.in.Institution.CreateInstitutionUseCase;
import com.keax.domain.ports.in.Institution.DeleteInstitutionUseCase;
import com.keax.domain.ports.in.Institution.UpdateInstitutionUseCase;
import com.keax.application.services.Interfaces.IInstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Institution;
import java.util.List;

@Service
public class InstitutionServiceImpl implements IInstitutionService {

    @Autowired
    private CreateInstitutionUseCase createInstitutionUseCase;

    @Autowired
    private UpdateInstitutionUseCase updateInstitutionUseCase;

    @Autowired
    private RetrieveInstitutionUseCase retrieveInstitutionUseCase;

    @Autowired
    private DeleteInstitutionUseCase deleteInstitutionUseCase;

    @Override
    public Institution createInstitution(Institution institution) {
        return createInstitutionUseCase.createInstitution(institution);
    }

    @Override
    public Institution updateInstitution(Long institution_id, Institution institution) {
        return updateInstitutionUseCase.updateInstitution(institution_id, institution);
    }

    @Override
    public List<Institution> getListInstitution() {
        return retrieveInstitutionUseCase.getListInstitution();
    }

    @Override
    public List<Institution> findByInstitutionDeleted(Boolean deleted) {
        return retrieveInstitutionUseCase.findByInstitutionDeleted(deleted);
    }

    @Override
    public Boolean deleteInstitution(Long institution_id) {
        return deleteInstitutionUseCase.deleteInstitution(institution_id);
    }

}
