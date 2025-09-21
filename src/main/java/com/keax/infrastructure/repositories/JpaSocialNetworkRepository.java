package com.keax.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.keax.infrastructure.entities.SocialNetworkEntity;
import java.util.Optional;
import java.util.List;

public interface JpaSocialNetworkRepository extends JpaRepository<SocialNetworkEntity, Long> {

    List<SocialNetworkEntity> findBySocialNetworkDeleted(Boolean deleted);
    Optional<SocialNetworkEntity> findBySocialNetworkNameAndSocialNetworkDeleted(String socialNetworkName, Boolean deleted);
    Optional<SocialNetworkEntity> findBySocialNetworkIdAndSocialNetworkDeleted(Long socialNetworkId, Boolean deleted);
    Optional<SocialNetworkEntity> findBySocialNetworkPositionAndSocialNetworkDeleted(int position, Boolean deleted);

}
