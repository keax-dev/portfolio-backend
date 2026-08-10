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
    private Boolean educationVisible;
    private Long institutionId;
    private String institutionName;
    private String institutionNameEs;
    private String institutionUrl;
    private Long version;

    public Education(
            Long educationId,
            String educationTitle,
            String educationTitleEs,
            String educationPlace,
            String educationStart,
            String educationStartEs,
            String educationEnd,
            String educationEndEs,
            int educationPosition,
            Long institutionId,
            String institutionName,
            String institutionNameEs,
            String institutionUrl,
            Long version
    ) {
        this(
                educationId,
                educationTitle,
                educationTitleEs,
                educationPlace,
                educationStart,
                educationStartEs,
                educationEnd,
                educationEndEs,
                educationPosition,
                true,
                institutionId,
                institutionName,
                institutionNameEs,
                institutionUrl,
                version
        );
    }

}
