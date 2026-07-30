package com.keax.course.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    private Long courseId;
    private String courseName;
    private String courseNameEn;
    private String courseCertificateImg;
    private String courseCertificateUrl;
    private Boolean courseDeleted;
    private Long institutionId;
    private String institutionName;
    private String institutionNameEs;
}
