package com.keax.infrastructure.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keax.application.services.Interfaces.IAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.keax.infrastructure.controllers.DTO.ApiResponse;
import org.springframework.http.ResponseEntity;
import com.keax.domain.models.Auth;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Auth>> login(@Valid @RequestBody Auth auth) {

        ApiResponse<Auth> response = new ApiResponse<>(
                true,
                "Welcome",
                authService.login(auth)
        );

        return ResponseEntity.ok(response);
    }

}
