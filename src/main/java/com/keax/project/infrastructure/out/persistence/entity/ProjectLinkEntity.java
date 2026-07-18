package com.keax.project.infrastructure.out.persistence.entity;

import com.keax.project.domain.model.ProjectLinkType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_link_id")
    private Long projectLinkId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_link_type", nullable = false, length = 40)
    private ProjectLinkType type;

    @Column(name = "project_link_url", nullable = false, length = 2048)
    private String url;

    @Column(name = "project_link_position", nullable = false)
    private int position;
}
