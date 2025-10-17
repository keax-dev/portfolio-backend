package com.keax.domain.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth {

    @NotBlank(message = "The username is required")
    private String username;

    @NotBlank(message = "The password is required")
    private String password;

    private String token;

}
