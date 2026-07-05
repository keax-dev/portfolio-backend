package com.keax.visitor.infrastructure.in.web.controller;

import com.keax.visitor.infrastructure.in.web.dto.RegisterVisitorRequestDTO;
import com.keax.visitor.infrastructure.in.web.dto.VisitorDashboardDTO;
import com.keax.visitor.infrastructure.in.web.resolver.ClientIpResolver;
import com.keax.visitor.infrastructure.in.web.mapper.VisitorWebMapper;
import com.keax.visitor.domain.ports.in.RegisterVisitorUseCase;
import com.keax.visitor.infrastructure.in.web.dto.VisitorDTO;
import com.keax.visitor.domain.ports.in.RetrieveVisitorUseCase;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RegisterVisitorUseCase registerVisitorUseCase;

    @Autowired
    private RetrieveVisitorUseCase retrieveVisitorUseCase;

    @Autowired
    private ClientIpResolver clientIpResolver;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<VisitorDTO>> register(
            @Valid @RequestBody(required = false) RegisterVisitorRequestDTO requestDTO,
            HttpServletRequest request
    ) {
        String ipAddress = clientIpResolver.resolve(request);

        ApiResponseDTO<VisitorDTO> response = registerVisitorUseCase.registerVisitor(
                        VisitorWebMapper.toDomain(requestDTO, ipAddress, request.getHeader("User-Agent"))
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
        Instant resolvedEndAt = resolveEndAt(endAt);
        Instant resolvedStartAt = resolveStartAt(startAt, resolvedEndAt);

        ApiResponseDTO<List<VisitorDTO>> response = new ApiResponseDTO<>(
                true,
                "Visitors found successfully",
                retrieveVisitorUseCase.getVisitorList(resolvedStartAt, resolvedEndAt).stream().map(VisitorWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponseDTO<VisitorDashboardDTO>> dashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endAt
    ) {
        Instant resolvedEndAt = resolveEndAt(endAt);
        Instant resolvedStartAt = resolveStartAt(startAt, resolvedEndAt);

        ApiResponseDTO<VisitorDashboardDTO> response = new ApiResponseDTO<>(
                true,
                "Visitor dashboard found successfully",
                VisitorWebMapper.dashboardFromDomain(retrieveVisitorUseCase.getDashboard(resolvedStartAt, resolvedEndAt))
        );

        return ResponseEntity.ok(response);
    }

    private Instant resolveStartAt(Instant startAt, Instant endAt) {
        if (startAt != null) {
            return startAt;
        }

        return endAt.minus(15, ChronoUnit.DAYS);
    }

    private Instant resolveEndAt(Instant endAt) {
        if (endAt != null) {
            return endAt;
        }

        return Instant.now();
    }

}
