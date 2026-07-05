package com.keax.visitor.infrastructure.in.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
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

    public String resolve(HttpServletRequest request) {
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
