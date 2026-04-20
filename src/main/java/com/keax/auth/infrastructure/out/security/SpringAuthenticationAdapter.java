package com.keax.auth.infrastructure.out.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.auth.domain.ports.out.AuthenticationPort;
import org.springframework.stereotype.Component;

@Component
public class SpringAuthenticationAdapter implements AuthenticationPort {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public boolean authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }

}
