package com.keax.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Institution {

    private Long institution_id;
    private String institution_name;
    private Boolean institution_deleted = false;

}
