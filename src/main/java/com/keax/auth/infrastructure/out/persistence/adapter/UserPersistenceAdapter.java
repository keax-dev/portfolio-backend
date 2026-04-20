package com.keax.auth.infrastructure.out.persistence.adapter;

import com.keax.auth.infrastructure.out.persistence.mapper.UserPersistenceMapper;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.auth.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Repository;
import com.keax.auth.domain.model.User;
import java.util.Optional;

@Repository
public class UserPersistenceAdapter implements UserRepositoryPort {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username).map(
                UserPersistenceMapper::toDomain
        );
    }

}
