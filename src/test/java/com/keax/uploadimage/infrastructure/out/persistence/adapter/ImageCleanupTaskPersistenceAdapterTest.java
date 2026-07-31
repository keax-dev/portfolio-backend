package com.keax.uploadimage.infrastructure.out.persistence.adapter;

import com.keax.uploadimage.infrastructure.out.persistence.entity.ImageCleanupTaskEntity;
import com.keax.uploadimage.infrastructure.out.persistence.repository.JpaImageCleanupTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageCleanupTaskPersistenceAdapterTest {

    private static final Instant NOW = Instant.parse("2026-07-30T12:00:00Z");

    private JpaImageCleanupTaskRepository repository;
    private ImageCleanupTaskPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(JpaImageCleanupTaskRepository.class);
        adapter = new ImageCleanupTaskPersistenceAdapter(
                repository,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
    }

    @Test
    void enqueuesOnlyDistinctNonBlankMissingUrls() {
        when(repository.findByImageUrl("existing")).thenReturn(Optional.of(task("existing")));
        when(repository.findByImageUrl("new")).thenReturn(Optional.empty());

        adapter.enqueueAll(List.of("", " ", "existing", "new", "new"));

        ArgumentCaptor<ImageCleanupTaskEntity> captor =
                ArgumentCaptor.forClass(ImageCleanupTaskEntity.class);
        verify(repository).save(captor.capture());
        ImageCleanupTaskEntity saved = captor.getValue();
        assertEquals("new", saved.getImageUrl());
        assertEquals(0, saved.getAttempts());
        assertNull(saved.getLastError());
        assertEquals(NOW, saved.getCreatedAt());
        assertEquals(NOW, saved.getUpdatedAt());
    }

    @Test
    void ignoresNullCollections() {
        adapter.enqueueAll(null);

        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void returnsOldestUrlsAndCompletesByUrl() {
        when(repository.findTop25ByOrderByCreatedAtAsc())
                .thenReturn(List.of(task("first"), task("second")));

        assertEquals(List.of("first", "second"), adapter.findOldestPendingUrls());

        adapter.complete("first");
        verify(repository).deleteByImageUrl("first");
    }

    @Test
    void recordsFailureWithIncrementedAttemptsAndTruncatedMessage() {
        ImageCleanupTaskEntity task = task("image");
        task.setAttempts(2);
        when(repository.findByImageUrl("image")).thenReturn(Optional.of(task));
        String error = "x".repeat(1001);

        adapter.recordFailure("image", error);

        assertEquals(3, task.getAttempts());
        assertEquals(1000, task.getLastError().length());
        assertEquals(NOW, task.getUpdatedAt());
        verify(repository).save(task);
    }

    @Test
    void handlesMissingTasksAndNullErrors() {
        when(repository.findByImageUrl("missing")).thenReturn(Optional.empty());
        ImageCleanupTaskEntity task = task("image");
        when(repository.findByImageUrl("image")).thenReturn(Optional.of(task));

        adapter.recordFailure("missing", "failure");
        adapter.recordFailure("image", null);

        assertNull(task.getLastError());
        verify(repository).save(task);
    }

    private ImageCleanupTaskEntity task(String url) {
        return new ImageCleanupTaskEntity(1L, url, 0, null, NOW, NOW);
    }
}
