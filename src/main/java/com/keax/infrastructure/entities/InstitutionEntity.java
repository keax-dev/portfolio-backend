package com.keax.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "institution")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "institution_id")
    private Long institutionId;

    @Column(name = "institution_name")
    private String institutionName;

    @Column(name = "institution_url")
    private String institutionUrl;

    @Column(name = "institution_deleted")
    private Boolean institutionDeleted;

}
