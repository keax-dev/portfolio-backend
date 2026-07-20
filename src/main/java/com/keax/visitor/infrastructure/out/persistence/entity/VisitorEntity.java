package com.keax.visitor.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "visitor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visitor_id")
    private Long visitorId;

    @Column(name = "visitor_ip", nullable = false, length = 64)
    private String visitorIp;

    @Column(name = "visitor_country", length = 120)
    private String visitorCountry;

    @Column(name = "visitor_city", length = 120)
    private String visitorCity;

    @Column(name = "visitor_user_agent", length = 500)
    private String visitorUserAgent;

    @Column(name = "visitor_path", length = 255)
    private String visitorPath;

    @Column(name = "visitor_visited_at", nullable = false)
    private Instant visitorVisitedAt;

}
