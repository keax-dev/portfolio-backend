package com.keax.domain.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Institution {

    private Long institution_id;

    @NotBlank(message = "El nombre de la institución es obligatorio")
    private String institution_name;
    private Boolean institution_deleted = false;

}
