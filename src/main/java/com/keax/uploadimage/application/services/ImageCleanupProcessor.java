package com.keax.uploadimage.application.services;

import com.keax.uploadimage.domain.ports.out.ImageCleanupTaskPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ImageCleanupProcessor {

    private static final Logger log = LoggerFactory.getLogger(ImageCleanupProcessor.class);

    private final ImageStoragePort imageStoragePort;
    private final ImageCleanupTaskPort cleanupTaskPort;

    @Scheduled(
            initialDelayString = "${app.image-cleanup.initial-delay-ms:60000}",
            fixedDelayString = "${app.image-cleanup.fixed-delay-ms:300000}"
    )
    public void processPending() {
        processQueuedUrls(cleanupTaskPort.findOldestPendingUrls());
    }

    public void processQueuedUrls(Collection<String> imageUrls) {
        if (imageUrls == null) {
            return;
        }
        imageUrls.stream()
                .filter(url -> url != null && !url.isBlank())
                .distinct()
                .forEach(this::processOne);
    }

    public void deleteOrEnqueue(Collection<String> imageUrls) {
        if (imageUrls == null) {
            return;
        }
        imageUrls.stream()
                .filter(url -> url != null && !url.isBlank())
                .distinct()
                .forEach(url -> {
                    try {
                        imageStoragePort.delete(url);
                    } catch (Exception ex) {
                        try {
                            cleanupTaskPort.enqueueAll(java.util.List.of(url));
                            cleanupTaskPort.recordFailure(url, ex.getMessage());
                        } catch (Exception queueException) {
                            log.error("Unable to persist image cleanup task. imageUrl={}", url, queueException);
                        }
                    }
                });
    }

    private void processOne(String imageUrl) {
        try {
            imageStoragePort.delete(imageUrl);
        } catch (Exception ex) {
            try {
                cleanupTaskPort.recordFailure(imageUrl, ex.getMessage());
            } catch (Exception persistenceException) {
                log.error("Unable to record image cleanup failure. imageUrl={}", imageUrl, persistenceException);
            }
            log.warn("Image cleanup remains pending. imageUrl={}", imageUrl, ex);
            return;
        }

        try {
            cleanupTaskPort.complete(imageUrl);
        } catch (Exception ex) {
            log.error("Image was removed but its cleanup task could not be completed. imageUrl={}", imageUrl, ex);
        }
    }
}
