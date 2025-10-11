package com.keax.domain.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @NotBlank(message = "The name is required")
    private String name;

    @NotBlank(message = "The email is required")
    private String email;

    @NotBlank(message = "The message is required")
    private String message;

}
