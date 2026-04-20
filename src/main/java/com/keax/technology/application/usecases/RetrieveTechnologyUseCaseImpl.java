package com.keax.technology.application.usecases;

import com.keax.technology.domain.ports.in.RetrieveTechnologyUseCase;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.technology.domain.model.Technology;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RetrieveTechnologyUseCaseImpl implements RetrieveTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public List<Technology> findByTechnologyDeleted(Boolean deleted) {

        List<Technology> technologyList =  technologyRepositoryPort.findByTechnologyDeleted(deleted);

        return validateNotEmpty(technologyList);
    }

    @Override
    public List<Technology> findByTechnologyDeletedWithProjects(Boolean deleted, Boolean projectDeleted) {

        List<Technology> technologyList =  technologyRepositoryPort.findByTechnologyDeletedWithProjects(deleted);

        technologyList.forEach(technology -> {
            List<Project> projectList = technology.getProjectList().stream().filter(p -> p.getProjectDeleted() == projectDeleted).toList();
            technology.setProjectList(projectList);
        });

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
