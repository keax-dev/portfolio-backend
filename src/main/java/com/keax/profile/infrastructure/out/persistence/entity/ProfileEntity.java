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

    @Column(name = "profile_specialties", nullable = false, length = 500)
    private String profileSpecialties;

    @Column(name = "profile_summary", nullable = false, columnDefinition = "text")
    private String profileSummary;

    @Column(name = "profile_summary_es", nullable = false, columnDefinition = "text")
    private String profileSummaryEs;

    @Column(name = "profile_about", nullable = false, columnDefinition = "text")
    private String profileAbout;

    @Column(name = "profile_about_es", nullable = false, columnDefinition = "text")
    private String profileAboutEs;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public ProfileEntity(
            Long profileId,
            String profileName,
            String profileLastName,
            String profileTitle,
            String profileTitleEs,
            String profileCv,
            String profileCvEs,
            String profilePicture
    ) {
        this(
                profileId,
                profileName,
                profileLastName,
                profileTitle,
                profileTitleEs,
                profileCv,
                profileCvEs,
                profilePicture,
                "",
                "",
                "",
                "",
                "",
                null
        );
    }

}
