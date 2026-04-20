package com.keax.auth.infrastructure.out.persistence.mapper;

import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.domain.model.User;

public final class UserPersistenceMapper {

    public static User toDomain(UserEntity entity) {
        return new User(
                entity.getUsername(),
                entity.getPassword()
        );
    }

}
