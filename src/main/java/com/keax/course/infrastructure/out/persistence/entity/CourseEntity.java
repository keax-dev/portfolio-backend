package com.keax.course.infrastructure.out.persistence.entity;

import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "course")
@SQLDelete(sql = "UPDATE course SET course_deleted = true WHERE course_id = ?")
@SQLRestriction("course_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name", nullable = false, length = 200)
    private String courseName;

    @Column(name = "course_name_en", nullable = false, length = 200)
    private String courseNameEn;

    @Column(name = "course_certificate_img", length = 2048)
    private String courseCertificateImg;

    @Column(name = "course_certificate_url", length = 2048)
    private String courseCertificateUrl;

    @Column(name = "course_deleted", nullable = false)
    private Boolean courseDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "institution_id", nullable = false)
    private InstitutionEntity institution;
}
