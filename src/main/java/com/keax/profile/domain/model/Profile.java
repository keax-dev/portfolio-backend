package com.keax.profile.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    private Long profileId;
    private String profileName;
    private String profileLastName;
    private String profileTitle;
    private String profileTitleEs;
    private String profileCv;
    private String profileCvEs;
    private String profilePicture;

}
