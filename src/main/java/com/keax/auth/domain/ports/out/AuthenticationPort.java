package com.keax.auth.domain.ports.out;

public interface AuthenticationPort {

    boolean authenticate(String username, String password);

}
