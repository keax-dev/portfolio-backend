package com.keax.project.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTechnology {

    private Long projectTechnologyId;
    private Long technologyId;
    private String technologyName;
    private int position;
}
