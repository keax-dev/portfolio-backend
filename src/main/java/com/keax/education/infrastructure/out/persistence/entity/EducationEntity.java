package com.keax.education.infrastructure.out.persistence.entity;

import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.jdbc.Expectation;

@Entity
@Table(name = "education")
@SQLDelete(
        sql = "UPDATE education SET education_deleted = true, version = version + 1 "
                + "WHERE education_id = ? AND version = ?",
        verify = Expectation.RowCount.class
)
@SQLRestriction("education_deleted = false")
@Getter
@Setter
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
    private Boolean educationDeleted = false;

    @Column(name = "education_visible", nullable = false)
    private Boolean educationVisible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private InstitutionEntity institution;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public EducationEntity(
            Long educationId,
            String educationTitle,
            String educationTitleEs,
            String educationPlace,
            String educationStart,
            String educationStartEs,
            String educationEnd,
            String educationEndEs,
            int educationPosition,
            Boolean educationDeleted,
            InstitutionEntity institution
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
                educationDeleted,
                true,
                institution,
                null
        );
    }

}
