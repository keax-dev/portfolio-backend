package com.keax.auth.application.usecases;

import org.springframework.transaction.annotation.Transactional;
import com.keax.auth.domain.ports.out.AuthenticationPort;
import com.keax.auth.domain.ports.out.TokenProviderPort;
import com.keax.shared.domain.exceptions.AuthenticationFailedException;
import com.keax.auth.domain.ports.in.AuthLoginUseCase;
import org.springframework.stereotype.Service;
import com.keax.auth.domain.model.Auth;

@Service
@Transactional(readOnly = true)
public class AuthLoginUseCaseImpl implements AuthLoginUseCase {

    private final AuthenticationPort authenticationPort;
    private final TokenProviderPort tokenProviderPort;

    public AuthLoginUseCaseImpl(AuthenticationPort authenticationPort, TokenProviderPort tokenProviderPort) {
        this.authenticationPort = authenticationPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public Auth login(Auth auth) {

        if (!authenticationPort.authenticate(auth.getUsername(), auth.getPassword())) {
            throw new AuthenticationFailedException("Incorrect username or password");
        }

        auth.setToken(tokenProviderPort.generateToken(auth.getUsername()));
        return auth;
    }

}
