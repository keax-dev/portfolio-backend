package com.keax.auth.domain.ports.out;

import java.util.Optional;

public interface TokenVerifierPort {

    Optional<String> extractSubject(String token);
}
