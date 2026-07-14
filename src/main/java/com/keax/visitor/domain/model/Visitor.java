package com.keax.visitor.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Visitor {

    private Long visitorId;
    private String visitorIp;
    private String visitorCountry;
    private String visitorCity;
    private String visitorUserAgent;
    private String visitorPath;
    private Instant visitorVisitedAt;

}
