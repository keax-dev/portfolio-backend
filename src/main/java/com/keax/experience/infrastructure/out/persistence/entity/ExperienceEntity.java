package com.keax.experience.infrastructure.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.jdbc.Expectation;

@Entity
@Table(name = "experience")
@SQLDelete(
        sql = "UPDATE experience SET experience_deleted = true, version = version + 1 "
                + "WHERE experience_id = ? AND version = ?",
        verify = Expectation.RowCount.class
)
@SQLRestriction("experience_deleted = false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private Long experienceId;

    @Column(name = "experience_role", nullable = false, length = 160)
    private String experienceRole;

    @Column(name = "experience_role_es", nullable = false, length = 160)
    private String experienceRoleEs;

    @Column(name = "experience_company", nullable = false, length = 160)
    private String experienceCompany;

    @Column(name = "experience_description", nullable = false, columnDefinition = "text")
    private String experienceDescription;

    @Column(name = "experience_description_es", nullable = false, columnDefinition = "text")
    private String experienceDescriptionEs;

    @Column(name = "experience_start", nullable = false, length = 80)
    private String experienceStart;

    @Column(name = "experience_start_es", nullable = false, length = 80)
    private String experienceStartEs;

    @Column(name = "experience_end", length = 80)
    private String experienceEnd;

    @Column(name = "experience_end_es", length = 80)
    private String experienceEndEs;

    @Column(name = "experience_position", nullable = false)
    private int experiencePosition;

    @Column(name = "experience_visible", nullable = false)
    private Boolean experienceVisible = true;

    @Column(name = "experience_deleted", nullable = false)
    private Boolean experienceDeleted = false;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
