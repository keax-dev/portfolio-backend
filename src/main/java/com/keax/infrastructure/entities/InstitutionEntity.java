package com.keax.infrastructure.entities;

import com.keax.domain.models.Institution;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @Column(name = "institution_deleted")
    private Boolean institutionDeleted;

    public static InstitutionEntity fromDomainModel(Institution institution){
        return  new InstitutionEntity(institution.getInstitution_id(), institution.getInstitution_name(), institution.getInstitution_deleted());
    }

    public Institution toDomainModel(){
        return new Institution(institutionId, institutionName, institutionDeleted);
    }

}
