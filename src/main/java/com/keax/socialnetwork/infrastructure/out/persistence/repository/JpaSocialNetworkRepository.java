package com.keax.socialnetwork.infrastructure.out.persistence.repository;

import com.keax.socialnetwork.infrastructure.out.persistence.entity.SocialNetworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface JpaSocialNetworkRepository extends JpaRepository<SocialNetworkEntity, Long> {

    List<SocialNetworkEntity> findAllByOrderBySocialNetworkPositionAsc();
    Optional<SocialNetworkEntity> findBySocialNetworkName(String socialNetworkName);
    Optional<SocialNetworkEntity> findBySocialNetworkPosition(int position);

}
