package com.keax.technology.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "technology")
@SQLDelete(sql = "UPDATE technology SET technology_deleted = true WHERE technology_id = ?")
@SQLRestriction("technology_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technology_id")
    private Long technologyId;

    @Column(name = "technology_name", nullable = false)
    private String technologyName;

    @Column(name = "technology_deleted", nullable = false)
    private Boolean technologyDeleted = false;

}
