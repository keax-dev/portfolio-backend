package com.keax.application.usecases.Technology;

import com.keax.domain.ports.in.Technology.RetrieveTechnologyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Technology;
import java.util.List;

@Component
public class RetrieveTechnologyUseCaseImpl implements RetrieveTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;


    @Override
    public List<Technology> findByTechnologyDeleted(Boolean deleted) {

        List<Technology> technologyList =  technologyRepositoryPort.findByTechnologyDeleted(deleted);

        return validateNotEmpty(technologyList);
    }

    @Override
    public List<Technology> findByTechnologyDeletedWithProjects(Boolean deleted) {

        List<Technology> technologyList =  technologyRepositoryPort.findByTechnologyDeletedWithProjects(deleted);

        return validateNotEmpty(technologyList);
    }

    @Override
    public List<Technology> getListTechnology() {

        List<Technology> technologyList =  technologyRepositoryPort.getListTechnology();

        return validateNotEmpty(technologyList);
    }

    private List<Technology> validateNotEmpty(List<Technology> technologyList) {

        if (technologyList.isEmpty()) {
            throw new ExceptionAlert("There are no created technologies");
        }

        return technologyList;
    }

}
