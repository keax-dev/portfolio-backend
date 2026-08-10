package com.keax.skill.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.jdbc.Expectation;
import com.keax.skill.domain.model.SkillCategory;

@Entity
@Table(name = "skill")
@SQLDelete(
        sql = "UPDATE skill SET skill_deleted = true, version = version + 1 "
                + "WHERE skill_id = ? AND version = ?",
        verify = Expectation.RowCount.class
)
@SQLRestriction("skill_deleted = false")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_category", nullable = false, length = 40)
    private SkillCategory skillCategory = SkillCategory.OTHER;

    @Column(name = "skill_visible", nullable = false)
    private Boolean skillVisible = true;

    @Column(name = "skill_deleted", nullable = false)
    private Boolean skillDeleted = false;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public SkillEntity(
            Long skillId,
            String skillName,
            String skillPicture,
            int skillPosition,
            Boolean skillDeleted
    ) {
        this(
                skillId,
                skillName,
                skillPicture,
                skillPosition,
                SkillCategory.OTHER,
                true,
                skillDeleted,
                null
        );
    }

}
