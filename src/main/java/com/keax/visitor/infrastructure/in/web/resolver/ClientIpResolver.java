package com.keax.visitor.infrastructure.in.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class ClientIpResolver {

    private static final List<String> IP_HEADERS = List.of(
            "CF-Connecting-IP",
            "X-Forwarded-For",
            "X-Real-IP"
    );
    private final boolean trustForwardedHeaders;

    public ClientIpResolver(
            @Value("${app.client-ip.trust-forwarded-headers:false}") boolean trustForwardedHeaders
    ) {
        this.trustForwardedHeaders = trustForwardedHeaders;
    }

    public String resolve(HttpServletRequest request) {
        if (!trustForwardedHeaders) {
            return request.getRemoteAddr();
        }

        return IP_HEADERS.stream()
                .map(request::getHeader)
                .map(this::firstIpFromHeader)
                .filter(this::hasText)
                .findFirst()
                .orElse(request.getRemoteAddr());
    }

    private String firstIpFromHeader(String headerValue) {
        if (!hasText(headerValue)) {
            return null;
        }

        return Arrays.stream(headerValue.split(","))
                .map(String::trim)
                .filter(this::hasText)
                .filter(value -> !"unknown".equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}
