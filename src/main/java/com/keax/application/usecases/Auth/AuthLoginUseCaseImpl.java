package com.keax.application.usecases.Auth;

import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.security.utils.JwtUtil;
import com.keax.domain.ports.in.Auth.AuthLoginUseCase;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Auth;

@Component
public class AuthLoginUseCaseImpl implements AuthLoginUseCase {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Auth login(Auth auth) {
        try{
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword())
            );

            auth.setToken(jwtUtil.generateToken(auth.getUsername()));
            return auth;

        }catch (AuthenticationException authExc){
            throw new ExceptionAlert("Incorrect username or password");
        }
    }

}
