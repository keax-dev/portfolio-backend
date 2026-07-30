package com.keax.institution.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "institution")
@SQLDelete(sql = "UPDATE institution SET institution_deleted = true WHERE institution_id = ?")
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

}
