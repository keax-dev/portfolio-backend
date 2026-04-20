package com.keax.institution.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Institution {

    private Long institutionId;
    private String institutionName;
    private String institutionNameEs;
    private String institutionUrl;
    private Boolean institutionDeleted;

}
