package com.keax.visitor.domain.ports.in;

import com.keax.visitor.domain.model.VisitorDashboard;
import com.keax.visitor.domain.model.Visitor;
import java.time.Instant;
import java.util.List;

public interface RetrieveVisitorUseCase {

    List<Visitor> getVisitorList(Instant startAt, Instant endAt);

    VisitorDashboard getDashboard(Instant startAt, Instant endAt);

}
