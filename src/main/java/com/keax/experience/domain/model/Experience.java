package com.keax.experience.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Experience {

    private Long experienceId;
    private String experienceRole;
    private String experienceRoleEs;
    private String experienceCompany;
    private String experienceDescription;
    private String experienceDescriptionEs;
    private String experienceStart;
    private String experienceStartEs;
    private String experienceEnd;
    private String experienceEndEs;
    private int experiencePosition;
    private Boolean experienceVisible;
    private Long version;
}
