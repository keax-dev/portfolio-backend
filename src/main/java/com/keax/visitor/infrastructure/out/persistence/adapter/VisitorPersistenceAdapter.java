package com.keax.visitor.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;

import com.keax.visitor.infrastructure.out.persistence.mapper.VisitorPersistenceMapper;
import com.keax.visitor.infrastructure.out.persistence.repository.JpaVisitorRepository;
import com.keax.visitor.infrastructure.out.persistence.repository.VisitorCountryCountProjection;
import com.keax.visitor.infrastructure.out.persistence.repository.VisitorCityCountProjection;
import com.keax.visitor.domain.ports.out.VisitorRepositoryPort;
import com.keax.visitor.domain.model.VisitorCountryCount;
import com.keax.visitor.domain.model.VisitorCityCount;
import com.keax.visitor.infrastructure.out.persistence.entity.VisitorEntity;
import com.keax.visitor.domain.model.Visitor;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VisitorPersistenceAdapter implements VisitorRepositoryPort {
    private final JpaVisitorRepository jpaVisitorRepository;

    @Override
    public Visitor saveVisitor(Visitor visitor) {
        VisitorEntity saved = jpaVisitorRepository.save(
                VisitorPersistenceMapper.toEntity(visitor)
        );
        return VisitorPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Visitor> findLatestByVisitorIp(String visitorIp) {
        return jpaVisitorRepository.findTopByVisitorIpOrderByVisitorVisitedAtDesc(visitorIp)
                .map(VisitorPersistenceMapper::toDomain);
    }

    @Override
    public List<Visitor> getVisitorList(Instant startAt, Instant endAt) {
        return jpaVisitorRepository.findByVisitorVisitedAtBetweenOrderByVisitorVisitedAtDesc(startAt, endAt).stream()
                .map(VisitorPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public long countVisitors(Instant startAt, Instant endAt) {
        return jpaVisitorRepository.countByVisitorVisitedAtBetween(startAt, endAt);
    }

    @Override
    public long countUniqueVisitorIps(Instant startAt, Instant endAt) {
        return jpaVisitorRepository.countUniqueVisitorIps(startAt, endAt);
    }

    @Override
    public long countByVisitorVisitedAtAfter(Instant visitedAt) {
        return jpaVisitorRepository.countByVisitorVisitedAtAfter(visitedAt);
    }

    @Override
    public List<VisitorCountryCount> countByCountry(Instant startAt, Instant endAt) {
        return jpaVisitorRepository.countByCountry(startAt, endAt).stream()
                .map(this::toCountryCount)
                .toList();
    }

    @Override
    public List<VisitorCityCount> countByCity(Instant startAt, Instant endAt) {
        return jpaVisitorRepository.countByCity(startAt, endAt).stream()
                .map(this::toCityCount)
                .toList();
    }

    private VisitorCountryCount toCountryCount(VisitorCountryCountProjection projection) {
        return new VisitorCountryCount(
                projection.getCountry(),
                projection.getTotal()
        );
    }

    private VisitorCityCount toCityCount(VisitorCityCountProjection projection) {
        return new VisitorCityCount(
                projection.getCity(),
                projection.getTotal()
        );
    }

}
