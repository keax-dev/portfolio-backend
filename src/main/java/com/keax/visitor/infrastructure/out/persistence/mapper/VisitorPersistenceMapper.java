package com.keax.visitor.infrastructure.out.persistence.mapper;

import com.keax.visitor.infrastructure.out.persistence.entity.VisitorEntity;
import com.keax.visitor.domain.model.Visitor;

public final class VisitorPersistenceMapper {

    private VisitorPersistenceMapper() {
    }

    public static VisitorEntity toEntity(Visitor visitor) {
        return new VisitorEntity(
                visitor.getVisitorId(),
                visitor.getVisitorIp(),
                visitor.getVisitorCountry(),
                visitor.getVisitorCity(),
                visitor.getVisitorUserAgent(),
                visitor.getVisitorPath(),
                visitor.getVisitorVisitedAt()
        );
    }

    public static Visitor toDomain(VisitorEntity entity) {
        return new Visitor(
                entity.getVisitorId(),
                entity.getVisitorIp(),
                entity.getVisitorCountry(),
                entity.getVisitorCity(),
                entity.getVisitorUserAgent(),
                entity.getVisitorPath(),
                entity.getVisitorVisitedAt()
        );
    }

}
