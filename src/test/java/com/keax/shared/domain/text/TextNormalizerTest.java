package com.keax.shared.domain.text;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TextNormalizerTest {

    @Test
    void normalizesTextWithLocaleIndependentRules() {
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));

            assertEquals("IDENTIFIER", TextNormalizer.uppercase(" identifier "));
            assertEquals("value", TextNormalizer.trimToNull(" value "));
        } finally {
            Locale.setDefault(previous);
        }
    }

    @Test
    void convertsNullAndBlankValuesToNull() {
        assertNull(TextNormalizer.uppercase(null));
        assertNull(TextNormalizer.uppercase("   "));
        assertNull(TextNormalizer.trimToNull(null));
        assertNull(TextNormalizer.trimToNull("\t"));
    }
}
