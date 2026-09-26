package dev.leandrojacome.webhook.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.net.URI;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class WebhookJobTest {
    @Test void rejects_non_https_destination() {
        assertThrows(IllegalArgumentException.class, () -> WebhookJob.pending(URI.create("http://example.test"), "{}", Instant.now()));
    }

    @Test void rejects_blank_payload() {
        assertThrows(IllegalArgumentException.class, () -> WebhookJob.pending(URI.create("https://example.test"), " ", Instant.now()));
    }
}
