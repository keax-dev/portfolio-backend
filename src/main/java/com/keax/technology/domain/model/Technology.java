package com.keax.technology.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Technology {

    private Long technologyId;
    private String technologyName;
    private Boolean technologyDeleted;

}
