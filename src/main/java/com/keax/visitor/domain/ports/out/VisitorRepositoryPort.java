package com.keax.visitor.domain.ports.out;

import com.keax.visitor.domain.model.VisitorCountryCount;
import com.keax.visitor.domain.model.VisitorCityCount;
import com.keax.visitor.domain.model.Visitor;
import java.time.Instant;
import java.util.Optional;
import java.util.List;

public interface VisitorRepositoryPort {

    Visitor saveVisitor(Visitor visitor);

    Optional<Visitor> findLatestByVisitorIp(String visitorIp);

    List<Visitor> getVisitorList(Instant startAt, Instant endAt);

    long countVisitors(Instant startAt, Instant endAt);

    long countUniqueVisitorIps(Instant startAt, Instant endAt);

    long countByVisitorVisitedAtAfter(Instant visitedAt);

    List<VisitorCountryCount> countByCountry(Instant startAt, Instant endAt);

    List<VisitorCityCount> countByCity(Instant startAt, Instant endAt);

}
