package com.keax.education.infrastructure.out.persistence.entity;

import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "education")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Long educationId;

    @Column(name = "education_title", nullable = false)
    private String educationTitle;

    @Column(name = "education_title_es")
    private String educationTitleEs;

    @Column(name = "education_place", nullable = false)
    private String educationPlace;

    @Column(name = "education_start")
    private String educationStart;

    @Column(name = "education_start_es")
    private String educationStartEs;

    @Column(name = "education_end", nullable = false)
    private  String educationEnd;

    @Column(name = "education_end_es")
    private  String educationEndEs;

    @Column(name = "education_position", nullable = false)
    private int educationPosition;

    @Column(name = "education_deleted", nullable = false)
    private Boolean educationDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private InstitutionEntity institution;

}
