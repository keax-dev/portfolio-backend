package com.keax.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "education_title")
    private String educationTitle;

    @Column(name = "education_place")
    private String educationPlace;

    @Column(name = "education_start")
    private String educationStart;

    @Column(name = "education_end")
    private  String educationEnd;

    @Column(name = "education_deleted")
    private Boolean educationDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private InstitutionEntity institution;

}
