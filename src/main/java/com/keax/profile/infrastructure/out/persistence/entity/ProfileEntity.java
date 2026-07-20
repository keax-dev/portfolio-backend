package com.keax.profile.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "profile_name", nullable = false)
    private String profileName;

    @Column(name = "profile_last_name", nullable = false)
    private String profileLastName;

    @Column(name = "profile_title", nullable = false)
    private String profileTitle;

    @Column(name = "profile_title_es")
    private String profileTitleEs;

    @Column(name = "profile_cv", length = 2048)
    private String profileCv;

    @Column(name = "profile_cv_es", length = 2048)
    private String profileCvEs;

    @Column(name = "profile_picture", length = 2048)
    private String profilePicture;

}
