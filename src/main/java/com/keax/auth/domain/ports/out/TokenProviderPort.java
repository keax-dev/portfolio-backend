package com.keax.auth.domain.ports.out;

public interface TokenProviderPort {

    String generateToken(String username);

}
