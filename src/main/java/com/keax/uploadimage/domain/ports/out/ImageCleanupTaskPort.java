package com.keax.uploadimage.domain.ports.out;

import java.util.Collection;
import java.util.List;

public interface ImageCleanupTaskPort {

    void enqueueAll(Collection<String> imageUrls);

    List<String> findOldestPendingUrls();

    void complete(String imageUrl);

    void recordFailure(String imageUrl, String error);
}
