package com.keax.visitor.infrastructure.out.persistence.repository;

import com.keax.visitor.infrastructure.out.persistence.entity.VisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.Optional;
import java.util.List;

public interface JpaVisitorRepository extends JpaRepository<VisitorEntity, Long> {

    Optional<VisitorEntity> findTopByVisitorIpOrderByVisitorVisitedAtDesc(String visitorIp);

    List<VisitorEntity> findByVisitorVisitedAtBetweenOrderByVisitorVisitedAtDesc(Instant startAt, Instant endAt);

    long countByVisitorVisitedAtBetween(Instant startAt, Instant endAt);

    long countByVisitorVisitedAtAfter(Instant visitedAt);

    @Query("""
            select count(distinct visitor.visitorIp)
            from VisitorEntity visitor
            where visitor.visitorVisitedAt between :startAt and :endAt
            """)
    long countUniqueVisitorIps(@Param("startAt") Instant startAt, @Param("endAt") Instant endAt);

    @Query("""
            select visitor.visitorCountry as country, count(visitor) as total
            from VisitorEntity visitor
            where visitor.visitorCountry is not null and visitor.visitorCountry <> ''
            and visitor.visitorVisitedAt between :startAt and :endAt
            group by visitor.visitorCountry
            order by count(visitor) desc
            """)
    List<VisitorCountryCountProjection> countByCountry(@Param("startAt") Instant startAt, @Param("endAt") Instant endAt);

    @Query("""
            select visitor.visitorCity as city, count(visitor) as total
            from VisitorEntity visitor
            where visitor.visitorCity is not null and visitor.visitorCity <> ''
            and visitor.visitorVisitedAt between :startAt and :endAt
            group by visitor.visitorCity
            order by count(visitor) desc
            """)
    List<VisitorCityCountProjection> countByCity(@Param("startAt") Instant startAt, @Param("endAt") Instant endAt);

}
