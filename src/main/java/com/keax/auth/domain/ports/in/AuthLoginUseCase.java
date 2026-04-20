package com.keax.auth.domain.ports.in;

import com.keax.auth.domain.model.Auth;

public interface AuthLoginUseCase {

    Auth login(Auth auth);

}
