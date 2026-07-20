package com.keax.uploadimage.infrastructure.out.persistence.adapter;

import com.keax.uploadimage.domain.ports.out.ImageCleanupTaskPort;
import com.keax.uploadimage.infrastructure.out.persistence.entity.ImageCleanupTaskEntity;
import com.keax.uploadimage.infrastructure.out.persistence.repository.JpaImageCleanupTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageCleanupTaskPersistenceAdapter implements ImageCleanupTaskPort {

    private final JpaImageCleanupTaskRepository repository;
    private final Clock clock;

    @Override
    @Transactional
    public void enqueueAll(Collection<String> imageUrls) {
        if (imageUrls == null) {
            return;
        }
        imageUrls.stream()
                .filter(url -> url != null && !url.isBlank())
                .distinct()
                .forEach(this::enqueueIfMissing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findOldestPendingUrls() {
        return repository.findTop25ByOrderByCreatedAtAsc().stream()
                .map(ImageCleanupTaskEntity::getImageUrl)
                .toList();
    }

    @Override
    @Transactional
    public void complete(String imageUrl) {
        repository.deleteByImageUrl(imageUrl);
    }

    @Override
    @Transactional
    public void recordFailure(String imageUrl, String error) {
        repository.findByImageUrl(imageUrl).ifPresent(task -> {
            task.setAttempts(task.getAttempts() + 1);
            task.setLastError(truncate(error, 1000));
            task.setUpdatedAt(clock.instant());
            repository.save(task);
        });
    }

    private void enqueueIfMissing(String imageUrl) {
        if (repository.findByImageUrl(imageUrl).isPresent()) {
            return;
        }
        Instant now = clock.instant();
        repository.save(new ImageCleanupTaskEntity(null, imageUrl, 0, null, now, now));
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
