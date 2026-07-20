package com.keax.auth.infrastructure.in.web.controller;

import lombok.RequiredArgsConstructor;

import com.keax.auth.infrastructure.in.web.mapper.AuthWebMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.keax.auth.infrastructure.in.web.dto.AuthDTO;
import com.keax.auth.domain.ports.in.AuthLoginUseCase;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import com.keax.auth.infrastructure.in.web.ratelimit.LoginRateLimiter;
import com.keax.shared.infrastructure.in.web.client.ClientIpResolver;
import com.keax.shared.infrastructure.in.web.client.ClientIdentityHasher;
import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthLoginUseCase authLoginUseCase;
    private final LoginRateLimiter loginRateLimiter;
    private final ClientIpResolver clientIpResolver;
    private final ClientIdentityHasher clientIdentityHasher;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthDTO>> login(
            @Valid @RequestBody AuthDTO auth,
            HttpServletRequest request
    ) {
        String clientKey = clientIdentityHasher.hash(clientIpResolver.resolve(request))
                + ":" + auth.getUsername().trim().toLowerCase(Locale.ROOT);
        loginRateLimiter.consumeAttempt(clientKey);

        ApiResponseDTO<AuthDTO> response = new ApiResponseDTO<>(
                true,
                "Welcome",
                AuthWebMapper.fromDomain(
                        authLoginUseCase.login(AuthWebMapper.toDomain(auth))
                )
        );

        loginRateLimiter.reset(clientKey);
        return ResponseEntity.ok(response);
    }

}
