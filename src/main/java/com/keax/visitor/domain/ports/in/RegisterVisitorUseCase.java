package com.keax.visitor.domain.ports.in;

import com.keax.visitor.domain.model.Visitor;
import java.util.Optional;

public interface RegisterVisitorUseCase {

    Optional<Visitor> registerVisitor(Visitor visitor);

}
