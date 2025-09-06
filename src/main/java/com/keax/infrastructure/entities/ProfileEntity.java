package com.keax.infrastructure.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "profile_last_name")
    private String profileLastName;

    @Column(name = "profile_title")
    private String profileTitle;

    @Column(name = "profile_cv")
    private String profileCv;

    @Column(name = "profile_picture")
    private String profilePicture;

}
