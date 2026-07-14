package com.keax.visitor.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitorCityCountDTO {

    @JsonProperty("city")
    private String city;

    @JsonProperty("total")
    private Long total;

}
