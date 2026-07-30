package com.keax.institution.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.jdbc.Expectation;

@Entity
@Table(name = "institution")
@SQLDelete(
        sql = "UPDATE institution SET institution_deleted = true, version = version + 1 "
                + "WHERE institution_id = ? AND version = ?",
        verify = Expectation.RowCount.class
)
@SQLRestriction("institution_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "institution_id")
    private Long institutionId;

    @Column(name = "institution_name", nullable = false)
    private String institutionName;

    @Column(name = "institution_name_es")
    private String institutionNameEs;

    @Column(name = "institution_url", length = 2048)
    private String institutionUrl;

    @Column(name = "institution_deleted", nullable = false)
    private Boolean institutionDeleted = false;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public InstitutionEntity(
            Long institutionId,
            String institutionName,
            String institutionNameEs,
            String institutionUrl,
            Boolean institutionDeleted
    ) {
        this(institutionId, institutionName, institutionNameEs, institutionUrl, institutionDeleted, null);
    }

}
