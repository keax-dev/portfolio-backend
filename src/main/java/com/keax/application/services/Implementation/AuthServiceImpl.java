package com.keax.application.services.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.application.services.Interfaces.IAuthService;
import com.keax.domain.ports.in.Auth.AuthLoginUseCase;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Auth;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private AuthLoginUseCase authLoginUseCase;

    @Override
    public Auth login(Auth auth) {
        return authLoginUseCase.login(auth);
    }

}
