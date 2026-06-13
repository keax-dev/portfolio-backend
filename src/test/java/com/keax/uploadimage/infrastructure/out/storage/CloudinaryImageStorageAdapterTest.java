package com.keax.uploadimage.infrastructure.out.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CloudinaryImageStorageAdapterTest {

    @Test
    void extractsPublicIdFromCloudinaryUrl() {
        String url = "https://res.cloudinary.com/keax/image/upload/v1777598984/Skills/vmkihyxzsgzfbguo50lv.jpg";

        assertEquals("Skills/vmkihyxzsgzfbguo50lv", CloudinaryImageStorageAdapter.extractPublicId(url));
    }

    @Test
    void extractsPublicIdWhenUrlHasQueryParams() {
        String url = "https://res.cloudinary.com/keax/image/upload/v1777598984/Profile/avatar.png?foo=bar";

        assertEquals("Profile/avatar", CloudinaryImageStorageAdapter.extractPublicId(url));
    }

    @Test
    void ignoresNonCloudinaryUploadUrl() {
        assertNull(CloudinaryImageStorageAdapter.extractPublicId("https://example.com/image.png"));
    }

}
