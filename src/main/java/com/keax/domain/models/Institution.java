package com.keax.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Institution {

    @JsonProperty("id")
    private Long institution_id;

    @JsonProperty("name")
    @NotBlank(message = "El nombre de la institución es obligatorio")
    private String institution_name;

    @JsonProperty("deleted")
    private Boolean institution_deleted = false;

}
