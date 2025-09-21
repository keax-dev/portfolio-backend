package com.keax.infrastructure.entities;

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

    @Column(name = "education_title", unique = true, nullable = false)
    private String educationTitle;

    @Column(name = "education_place", nullable = false)
    private String educationPlace;

    @Column(name = "education_start", nullable = false)
    private String educationStart;

    @Column(name = "education_end", nullable = false)
    private  String educationEnd;

    @Column(name = "education_position", nullable = false)
    private int educationPosition;

    @Column(name = "education_deleted", nullable = false)
    private Boolean educationDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private InstitutionEntity institution;

}
