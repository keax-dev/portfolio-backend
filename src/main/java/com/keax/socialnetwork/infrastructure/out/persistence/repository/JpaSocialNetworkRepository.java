package com.keax.socialnetwork.infrastructure.out.persistence.repository;

import com.keax.socialnetwork.infrastructure.out.persistence.entity.SocialNetworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface JpaSocialNetworkRepository extends JpaRepository<SocialNetworkEntity, Long> {

    List<SocialNetworkEntity> findBySocialNetworkDeleted(Boolean deleted);
    Optional<SocialNetworkEntity> findBySocialNetworkNameAndSocialNetworkDeleted(String socialNetworkName, Boolean deleted);
    Optional<SocialNetworkEntity> findBySocialNetworkIdAndSocialNetworkDeleted(Long socialNetworkId, Boolean deleted);
    Optional<SocialNetworkEntity> findBySocialNetworkPositionAndSocialNetworkDeleted(int position, Boolean deleted);

}
