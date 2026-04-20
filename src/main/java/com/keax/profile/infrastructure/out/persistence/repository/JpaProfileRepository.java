package com.keax.profile.infrastructure.out.persistence.repository;

import com.keax.profile.infrastructure.out.persistence.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProfileRepository extends JpaRepository<ProfileEntity, Long> {

}
