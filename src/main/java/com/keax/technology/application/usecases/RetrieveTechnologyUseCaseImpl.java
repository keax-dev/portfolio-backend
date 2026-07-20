package com.keax.technology.application.usecases;

import com.keax.technology.domain.ports.in.RetrieveTechnologyUseCase;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import com.keax.technology.domain.model.Technology;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RetrieveTechnologyUseCaseImpl implements RetrieveTechnologyUseCase {

    private final TechnologyRepositoryPort technologyRepositoryPort;

    public RetrieveTechnologyUseCaseImpl(TechnologyRepositoryPort technologyRepositoryPort) {
        this.technologyRepositoryPort = technologyRepositoryPort;
    }

    @Override
    public List<Technology> findByTechnologyDeleted(Boolean deleted) {

        return technologyRepositoryPort.findByTechnologyDeleted(deleted);
    }

    @Override
    public List<Technology> getListTechnology() {

        return technologyRepositoryPort.getListTechnology();
    }

}
