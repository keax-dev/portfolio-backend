package com.keax.uploadimage.infrastructure.out.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Verifica la extraccion segura del public id que Cloudinary necesita para
 * eliminar una imagen previamente almacenada.
 */
class CloudinaryImageStorageAdapterTest {

    @Test
    void extractsPublicIdFromCloudinaryUrl() {
        // Arrange: se usa una URL versionada dentro de una carpeta.
        String url = "https://res.cloudinary.com/keax/image/upload/v1777598984/Skills/vmkihyxzsgzfbguo50lv.jpg";

        // Act y Assert: se eliminan host, version y extension.
        assertEquals("Skills/vmkihyxzsgzfbguo50lv", CloudinaryImageStorageAdapter.extractPublicId(url));
    }

    @Test
    void extractsPublicIdWhenUrlHasQueryParams() {
        // Arrange: la URL incluye parametros que no forman parte del public id.
        String url = "https://res.cloudinary.com/keax/image/upload/v1777598984/Profile/avatar.png?foo=bar";

        // Act y Assert: tambien se descartan los parametros.
        assertEquals("Profile/avatar", CloudinaryImageStorageAdapter.extractPublicId(url));
    }

    @Test
    void ignoresNonCloudinaryUploadUrl() {
        // Act y Assert: una URL ajena no se envia al endpoint de destruccion.
        assertNull(CloudinaryImageStorageAdapter.extractPublicId("https://example.com/image.png"));
    }

}
