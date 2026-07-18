package com.keax.project.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectLink {

    private Long projectLinkId;
    private ProjectLinkType type;
    private String url;
    private int position;
}
