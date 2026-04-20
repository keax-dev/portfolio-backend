package com.keax.technology.domain.model;

import com.keax.project.domain.model.Project;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Technology {

    private Long technologyId;
    private String technologyName;
    private int technologyPosition;
    private Boolean technologyDeleted;
    private List<Project> projectList = new ArrayList<>();

}
