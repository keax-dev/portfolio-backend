package com.keax.visitor.application.usecases;

import com.keax.visitor.domain.ports.in.RegisterVisitorUseCase;
import com.keax.visitor.domain.ports.out.VisitorRepositoryPort;
import com.keax.visitor.domain.model.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class RegisterVisitorUseCaseImpl implements RegisterVisitorUseCase {

    @Autowired
    private VisitorRepositoryPort visitorRepositoryPort;

    @Value("${app.visitor.dedupe-window-minutes:30}")
    private long dedupeWindowMinutes;

    @Override
    public Optional<Visitor> registerVisitor(Visitor visitor) {
        Instant now = Instant.now();
        Instant dedupeLimit = now.minus(Duration.ofMinutes(dedupeWindowMinutes));

        Optional<Visitor> latestVisit = visitorRepositoryPort.findLatestByVisitorIp(visitor.getVisitorIp());

        if (latestVisit.isPresent() && latestVisit.get().getVisitorVisitedAt().isAfter(dedupeLimit)) {
            return Optional.empty();
        }

        visitor.setVisitorVisitedAt(now);

        return Optional.of(visitorRepositoryPort.saveVisitor(visitor));
    }

}
