package com.keax.visitor.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitorDashboardDTO {

    @JsonProperty("totalVisits")
    private Long totalVisits;

    @JsonProperty("uniqueVisitors")
    private Long uniqueVisitors;

    @JsonProperty("visitsLast24Hours")
    private Long visitsLast24Hours;

    @JsonProperty("countries")
    private List<VisitorCountryCountDTO> countries = new ArrayList<>();

    @JsonProperty("cities")
    private List<VisitorCityCountDTO> cities = new ArrayList<>();

}
