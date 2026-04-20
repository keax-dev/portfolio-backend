package com.keax.skill.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    private Long skillId;
    private String skillName;
    private String skillPicture;
    private int skillPosition;
    private Boolean skillDeleted;

}
