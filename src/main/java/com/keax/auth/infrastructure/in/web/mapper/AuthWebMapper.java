package com.keax.auth.infrastructure.in.web.mapper;

import com.keax.auth.infrastructure.in.web.dto.AuthDTO;
import com.keax.auth.domain.model.Auth;

public final class AuthWebMapper {

    public static Auth toDomain(AuthDTO dto) {
        return new Auth(
                dto.getUsername(),
                dto.getPassword(),
                dto.getToken()
        );
    }

    public static AuthDTO fromDomain(Auth auth) {
        return new AuthDTO(
                auth.getUsername(),
                null,
                auth.getToken()
        );
    }

}
