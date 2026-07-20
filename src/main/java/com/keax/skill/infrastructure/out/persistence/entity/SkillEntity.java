package com.keax.skill.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "skill_picture", length = 2048)
    private String skillPicture;

    @Column(name = "skill_position", nullable = false)
    private int skillPosition;

    @Column(name = "skill_deleted", nullable = false)
    private Boolean skillDeleted;

}
