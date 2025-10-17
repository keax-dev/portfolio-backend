package com.keax.infrastructure.adapters;

import com.keax.infrastructure.entities.UserEntity;
import com.keax.infrastructure.repositories.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Repository;
import com.keax.domain.models.User;
import java.util.Optional;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username).map(this::toDomainModel);
    }

    private User toDomainModel(UserEntity userEntity){
        return new User(userEntity.getUsername(), userEntity.getPassword());
    }

}
