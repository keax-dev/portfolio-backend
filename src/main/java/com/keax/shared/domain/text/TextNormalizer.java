package com.keax.shared.domain.text;

import java.util.Locale;

public final class TextNormalizer {

    private TextNormalizer() {
    }

    public static String uppercase(String value) {
        String normalized = trimToNull(value);
        return normalized == null ? null : normalized.toUpperCase(Locale.ROOT);
    }

    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    public static String trimToEmpty(String value) {
        String normalized = trimToNull(value);
        return normalized == null ? "" : normalized;
    }
}
