package com.keax.visitor.infrastructure.in.web.mapper;

import com.keax.visitor.infrastructure.in.web.dto.RegisterVisitorRequestDTO;
import com.keax.visitor.infrastructure.in.web.dto.VisitorCityCountDTO;
import com.keax.visitor.infrastructure.in.web.dto.VisitorCountryCountDTO;
import com.keax.visitor.infrastructure.in.web.dto.VisitorDashboardDTO;
import com.keax.visitor.infrastructure.in.web.dto.VisitorDTO;
import com.keax.visitor.domain.model.VisitorCityCount;
import com.keax.visitor.domain.model.VisitorCountryCount;
import com.keax.visitor.domain.model.VisitorDashboard;
import com.keax.visitor.domain.model.Visitor;

public final class VisitorWebMapper {

    private VisitorWebMapper() {
    }

    public static Visitor toDomain(RegisterVisitorRequestDTO dto, String ipAddress, String userAgent) {
        return new Visitor(
                null,
                ipAddress,
                trimToLength(dto == null ? null : dto.getCountry(), 120),
                trimToLength(dto == null ? null : dto.getCity(), 120),
                trimToLength(userAgent, 500),
                trimToLength(dto == null ? null : dto.getPath(), 255),
                null
        );
    }

    public static VisitorDTO fromDomain(Visitor visitor) {
        return new VisitorDTO(
                visitor.getVisitorId(),
                visitor.getVisitorIp(),
                visitor.getVisitorCountry(),
                visitor.getVisitorCity(),
                visitor.getVisitorUserAgent(),
                visitor.getVisitorPath(),
                visitor.getVisitorVisitedAt()
        );
    }

    public static VisitorDashboardDTO dashboardFromDomain(VisitorDashboard dashboard) {
        return new VisitorDashboardDTO(
                dashboard.getTotalVisits(),
                dashboard.getUniqueVisitors(),
                dashboard.getVisitsLast24Hours(),
                dashboard.getCountries().stream().map(VisitorWebMapper::countryFromDomain).toList(),
                dashboard.getCities().stream().map(VisitorWebMapper::cityFromDomain).toList()
        );
    }

    private static VisitorCountryCountDTO countryFromDomain(VisitorCountryCount countryCount) {
        return new VisitorCountryCountDTO(
                countryCount.getCountry(),
                countryCount.getTotal()
        );
    }

    private static VisitorCityCountDTO cityFromDomain(VisitorCityCount cityCount) {
        return new VisitorCityCountDTO(
                cityCount.getCity(),
                cityCount.getTotal()
        );
    }

    private static String trimToLength(String value, int maxLength) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        if (trimmed.length() <= maxLength) {
            return trimmed;
        }

        return trimmed.substring(0, maxLength);
    }

}
