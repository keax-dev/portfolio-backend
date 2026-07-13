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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthLoginUseCase authLoginUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthDTO>> login(@Valid @RequestBody AuthDTO auth) {
        ApiResponseDTO<AuthDTO> response = new ApiResponseDTO<>(
                true,
                "Welcome",
                AuthWebMapper.fromDomain(
                        authLoginUseCase.login(AuthWebMapper.toDomain(auth))
                )
        );

        return ResponseEntity.ok(response);
    }

}
