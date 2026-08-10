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
    private int coursePosition;
    private Boolean courseVisible;
    private Long institutionId;
    private String institutionName;
    private String institutionNameEs;
    private Long version;

    public Course(
            Long courseId,
            String courseName,
            String courseNameEn,
            String courseCertificateImg,
            String courseCertificateUrl,
            int coursePosition,
            Long institutionId,
            String institutionName,
            String institutionNameEs,
            Long version
    ) {
        this(
                courseId,
                courseName,
                courseNameEn,
                courseCertificateImg,
                courseCertificateUrl,
                coursePosition,
                true,
                institutionId,
                institutionName,
                institutionNameEs,
                version
        );
    }
}
