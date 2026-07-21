package com.keax.shared.infrastructure.in.web.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public final class ClientIpResolver {

    private static final List<String> IP_HEADERS = List.of(
            "CF-Connecting-IP",
            "X-Forwarded-For",
            "X-Real-IP"
    );
    private final boolean trustForwardedHeaders;
    private final Set<String> trustedProxies;

    @Autowired
    public ClientIpResolver(
            @Value("${app.client-ip.trust-forwarded-headers:false}") boolean trustForwardedHeaders,
            @Value("${app.client-ip.trusted-proxies:127.0.0.1,::1}") String trustedProxies
    ) {
        this.trustForwardedHeaders = trustForwardedHeaders;
        this.trustedProxies = parseTrustedProxies(trustedProxies);
    }

    public ClientIpResolver(boolean trustForwardedHeaders) {
        this.trustForwardedHeaders = trustForwardedHeaders;
        this.trustedProxies = Set.of("*");
    }

    public String resolve(HttpServletRequest request) {
        String remoteAddress = normalize(request.getRemoteAddr());
        if (!trustForwardedHeaders || !isTrustedProxy(remoteAddress)) {
            return remoteAddress;
        }

        return IP_HEADERS.stream()
                .map(request::getHeader)
                .map(this::firstIpFromHeader)
                .filter(this::hasText)
                .findFirst()
                .orElse(remoteAddress);
    }

    private String firstIpFromHeader(String headerValue) {
        if (!hasText(headerValue)) {
            return null;
        }

        return Arrays.stream(headerValue.split(","))
                .map(String::trim)
                .filter(this::hasText)
                .filter(value -> !"unknown".equalsIgnoreCase(value))
                .map(this::normalize)
                .filter(this::hasText)
                .findFirst()
                .orElse(null);
    }

    private boolean isTrustedProxy(String remoteAddress) {
        return trustedProxies.contains("*") || trustedProxies.contains(remoteAddress);
    }

    private Set<String> parseTrustedProxies(String value) {
        Set<String> proxies = new HashSet<>();
        if (value == null) {
            return proxies;
        }
        Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(this::hasText)
                .map(this::normalize)
                .filter(this::hasText)
                .forEach(proxies::add);
        return proxies;
    }

    public String normalize(String value) {
        if (!hasText(value) || value.length() > 45 || !value.matches("[0-9a-fA-F:.]+")) {
            return null;
        }

        try {
            InetAddress address = InetAddress.getByName(value);
            String normalized = address.getHostAddress();
            int scopeIndex = normalized.indexOf('%');
            return scopeIndex >= 0 ? normalized.substring(0, scopeIndex) : normalized;
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
