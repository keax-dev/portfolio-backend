package com.keax.visitor.infrastructure.in.web.controller;

import com.keax.visitor.infrastructure.in.web.dto.RegisterVisitorRequestDTO;
import com.keax.visitor.infrastructure.in.web.dto.VisitorDashboardDTO;
import com.keax.shared.infrastructure.in.web.client.ClientIpResolver;
import com.keax.shared.infrastructure.in.web.client.ClientIdentityHasher;
import com.keax.visitor.infrastructure.in.web.mapper.VisitorWebMapper;
import com.keax.visitor.domain.ports.in.RegisterVisitorUseCase;
import com.keax.visitor.infrastructure.in.web.dto.VisitorDTO;
import com.keax.visitor.domain.ports.in.RetrieveVisitorUseCase;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.temporal.ChronoUnit;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/visitor")
public class VisitorController {

    private final RegisterVisitorUseCase registerVisitorUseCase;
    private final RetrieveVisitorUseCase retrieveVisitorUseCase;
    private final ClientIpResolver clientIpResolver;
    private final ClientIdentityHasher clientIdentityHasher;

    public VisitorController(
            RegisterVisitorUseCase registerVisitorUseCase,
            RetrieveVisitorUseCase retrieveVisitorUseCase,
            ClientIpResolver clientIpResolver,
            ClientIdentityHasher clientIdentityHasher
    ) {
        this.registerVisitorUseCase = registerVisitorUseCase;
        this.retrieveVisitorUseCase = retrieveVisitorUseCase;
        this.clientIpResolver = clientIpResolver;
        this.clientIdentityHasher = clientIdentityHasher;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<VisitorDTO>> register(
            @Valid @RequestBody(required = false) RegisterVisitorRequestDTO requestDTO,
            HttpServletRequest request
    ) {
        String clientIdentity = clientIdentityHasher.hash(clientIpResolver.resolve(request));

        ApiResponseDTO<VisitorDTO> response = registerVisitorUseCase.registerVisitor(
                        VisitorWebMapper.toDomain(requestDTO, clientIdentity, request.getHeader("User-Agent"))
                )
                .map(visitor -> new ApiResponseDTO<>(
                        true,
                        "Visit registered successfully",
                        VisitorWebMapper.fromDomain(visitor)
                ))
                .orElseGet(() -> new ApiResponseDTO<>(
                        true,
                        "Visit was already registered recently",
                        null
                ));

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<VisitorDTO>>> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endAt
    ) {
        DateRange range = resolveRange(startAt, endAt);

        ApiResponseDTO<List<VisitorDTO>> response = new ApiResponseDTO<>(
                true,
                "Visitors found successfully",
                retrieveVisitorUseCase.getVisitorList(range.startAt(), range.endAt()).stream().map(VisitorWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponseDTO<VisitorDashboardDTO>> dashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endAt
    ) {
        DateRange range = resolveRange(startAt, endAt);

        ApiResponseDTO<VisitorDashboardDTO> response = new ApiResponseDTO<>(
                true,
                "Visitor dashboard found successfully",
                VisitorWebMapper.dashboardFromDomain(retrieveVisitorUseCase.getDashboard(range.startAt(), range.endAt()))
        );

        return ResponseEntity.ok(response);
    }

    private DateRange resolveRange(Instant startAt, Instant endAt) {
        Instant resolvedEndAt = endAt == null ? Instant.now() : endAt;
        Instant resolvedStartAt = startAt == null
                ? resolvedEndAt.minus(15, ChronoUnit.DAYS)
                : startAt;

        if (resolvedStartAt.isAfter(resolvedEndAt)) {
            throw new ExceptionAlert("The visitor start date must not be after the end date");
        }

        return new DateRange(resolvedStartAt, resolvedEndAt);
    }

    private record DateRange(Instant startAt, Instant endAt) {
    }

}
