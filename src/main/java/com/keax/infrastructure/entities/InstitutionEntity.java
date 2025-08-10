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
    private Long institution_id;
    private String institution_name;
    private Boolean institution_deleted;

    public static InstitutionEntity fromDomainModel(Institution institution){
        return  new InstitutionEntity(institution.getInstitution_id(), institution.getInstitution_name(), institution.getInstitution_deleted());
    }

    public Institution toDomainModel(){
        return new Institution(institution_id, institution_name, institution_deleted);
    }

}
