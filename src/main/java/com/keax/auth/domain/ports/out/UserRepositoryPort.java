package com.keax.auth.domain.ports.out;

import com.keax.auth.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByUsername(String username);

}
