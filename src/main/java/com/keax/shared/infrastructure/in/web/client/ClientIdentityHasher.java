package com.keax.shared.infrastructure.in.web.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Component
public final class ClientIdentityHasher {

    private static final String ALGORITHM = "HmacSHA256";
    private final SecretKeySpec secretKey;

    public ClientIdentityHasher(@Value("${app.visitor.ip-hash-secret}") String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("Visitor IP hash secret must have at least 32 characters");
        }
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    public String hash(String value) {
        try {
            String safeValue = value == null || value.isBlank() ? "unknown" : value;
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);
            return HexFormat.of().formatHex(mac.doFinal(safeValue.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to protect the client identity", ex);
        }
    }
}
