package com.keax.auth.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {

    @NotBlank(message = "The username is required")
    @Size(max = 120, message = "The username must not exceed 120 characters")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "The password is required")
    @Size(min = 8, max = 128, message = "The password must contain between 8 and 128 characters")
    private String password;

    private String token;

}
