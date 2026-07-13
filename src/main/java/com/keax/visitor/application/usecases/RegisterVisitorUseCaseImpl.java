package com.keax.visitor.application.usecases;

import com.keax.visitor.domain.ports.in.RegisterVisitorUseCase;
import com.keax.visitor.domain.ports.out.VisitorRepositoryPort;
import com.keax.visitor.domain.model.Visitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class RegisterVisitorUseCaseImpl implements RegisterVisitorUseCase {

    private final VisitorRepositoryPort visitorRepositoryPort;
    private final long dedupeWindowMinutes;
    private final Clock clock;

    public RegisterVisitorUseCaseImpl(
            VisitorRepositoryPort visitorRepositoryPort,
            @Value("${app.visitor.dedupe-window-minutes:30}")
            long dedupeWindowMinutes,
            Clock clock
    ) {
        if (dedupeWindowMinutes < 0) {
            throw new IllegalArgumentException("Visitor deduplication window cannot be negative");
        }

        this.visitorRepositoryPort = visitorRepositoryPort;
        this.dedupeWindowMinutes = dedupeWindowMinutes;
        this.clock = clock;
    }

    @Override
    public Optional<Visitor> registerVisitor(Visitor visitor) {
        Instant now = clock.instant();
        Instant dedupeLimit = now.minus(Duration.ofMinutes(dedupeWindowMinutes));

        Optional<Visitor> latestVisit = visitorRepositoryPort.findLatestByVisitorIp(visitor.getVisitorIp());

        if (latestVisit
                .map(Visitor::getVisitorVisitedAt)
                .filter(visitedAt -> !visitedAt.isBefore(dedupeLimit))
                .isPresent()) {
            return Optional.empty();
        }

        visitor.setVisitorVisitedAt(now);

        return Optional.of(visitorRepositoryPort.saveVisitor(visitor));
    }

}
