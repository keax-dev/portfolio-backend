package com.keax.visitor.application.usecases;

import com.keax.visitor.domain.ports.in.RetrieveVisitorUseCase;
import com.keax.visitor.domain.ports.out.VisitorRepositoryPort;
import com.keax.visitor.domain.model.VisitorDashboard;
import com.keax.visitor.domain.model.Visitor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.time.Duration;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RetrieveVisitorUseCaseImpl implements RetrieveVisitorUseCase {

    private final VisitorRepositoryPort visitorRepositoryPort;
    private final Clock clock;

    @Autowired
    public RetrieveVisitorUseCaseImpl(VisitorRepositoryPort visitorRepositoryPort) {
        this(visitorRepositoryPort, Clock.systemUTC());
    }

    RetrieveVisitorUseCaseImpl(VisitorRepositoryPort visitorRepositoryPort, Clock clock) {
        this.visitorRepositoryPort = visitorRepositoryPort;
        this.clock = clock;
    }

    @Override
    public List<Visitor> getVisitorList(Instant startAt, Instant endAt) {
        return visitorRepositoryPort.getVisitorList(startAt, endAt);
    }

    @Override
    public VisitorDashboard getDashboard(Instant startAt, Instant endAt) {
        return new VisitorDashboard(
                visitorRepositoryPort.countVisitors(startAt, endAt),
                visitorRepositoryPort.countUniqueVisitorIps(startAt, endAt),
                visitorRepositoryPort.countByVisitorVisitedAtAfter(clock.instant().minus(Duration.ofHours(24))),
                visitorRepositoryPort.countByCountry(startAt, endAt),
                visitorRepositoryPort.countByCity(startAt, endAt)
        );
    }

}
