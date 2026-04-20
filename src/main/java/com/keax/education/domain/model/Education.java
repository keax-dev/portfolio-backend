package com.keax.education.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    private Long educationId;
    private String educationTitle;
    private String educationTitleEs;
    private String educationPlace;
    private String educationStart;
    private String educationStartEs;
    private  String educationEnd;
    private  String educationEndEs;
    private int educationPosition;
    private Boolean educationDeleted;
    private Long institutionId;
    private String institutionName;
    private String institutionNameEs;
    private String institutionUrl;

}
