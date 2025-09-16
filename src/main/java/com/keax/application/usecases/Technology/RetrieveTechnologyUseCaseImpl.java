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

        if (technologyList.isEmpty()){
            throw new ExceptionAlert("There are no created technologies");
        }

        return technologyList;
    }

    @Override
    public List<Technology> getListTechnology() {

        List<Technology> technologyList =  technologyRepositoryPort.getListTechnology();

        if (technologyList.isEmpty()){
            throw new ExceptionAlert("There are no created technologies");
        }

        return technologyList;
    }

}
