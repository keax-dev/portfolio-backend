package com.keax.visitor.domain.model;

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
public class VisitorDashboard {

    private Long totalVisits;
    private Long uniqueVisitors;
    private Long visitsLast24Hours;
    private List<VisitorCountryCount> countries = new ArrayList<>();
    private List<VisitorCityCount> cities = new ArrayList<>();

}
