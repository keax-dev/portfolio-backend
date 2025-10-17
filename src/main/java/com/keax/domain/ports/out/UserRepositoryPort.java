package com.keax.domain.ports.out;

import com.keax.domain.models.User;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByUsername(String username);

}
