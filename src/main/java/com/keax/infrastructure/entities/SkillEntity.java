package com.keax.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "skill_name", unique = true, nullable = false)
    private String skillName;

    @Column(name = "skill_picture")
    private String skillPicture;

    @Column(name = "skill_deleted", nullable = false)
    private Boolean skillDeleted;

}
