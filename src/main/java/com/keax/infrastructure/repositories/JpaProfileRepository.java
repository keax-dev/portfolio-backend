package com.keax.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.keax.infrastructure.entities.ProfileEntity;

public interface JpaProfileRepository extends JpaRepository<ProfileEntity, Long> {

}
