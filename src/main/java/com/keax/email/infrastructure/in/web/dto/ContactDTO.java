package com.keax.email.infrastructure.in.web.dto;

import jakarta.validation.constraints.Email;
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
public class ContactDTO {

    @NotBlank(message = "The name is required")
    @Size(max = 120, message = "The name must not exceed 120 characters")
    private String name;

    @NotBlank(message = "The email is required")
    @Email(message = "The email format is invalid")
    @Size(max = 254, message = "The email must not exceed 254 characters")
    private String email;

    @NotBlank(message = "The message is required")
    @Size(max = 5000, message = "The message must not exceed 5000 characters")
    private String message;

}
