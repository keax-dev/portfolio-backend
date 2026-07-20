package com.keax.uploadimage.infrastructure.out.persistence.repository;

import com.keax.uploadimage.infrastructure.out.persistence.entity.ImageCleanupTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaImageCleanupTaskRepository extends JpaRepository<ImageCleanupTaskEntity, Long> {

    Optional<ImageCleanupTaskEntity> findByImageUrl(String imageUrl);

    List<ImageCleanupTaskEntity> findTop25ByOrderByCreatedAtAsc();

    void deleteByImageUrl(String imageUrl);
}
