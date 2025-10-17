package com.keax.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.keax.infrastructure.entities.UserEntity;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

}
