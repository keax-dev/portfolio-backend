package com.keax.visitor.infrastructure.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitorDTO {

    @JsonProperty("id")
    private Long visitorId;

    @JsonProperty("ip")
    private String visitorIp;

    @JsonProperty("country")
    private String visitorCountry;

    @JsonProperty("city")
    private String visitorCity;

    @JsonProperty("userAgent")
    private String visitorUserAgent;

    @JsonProperty("path")
    private String visitorPath;

    @JsonProperty("visitedAt")
    private Instant visitorVisitedAt;

}
