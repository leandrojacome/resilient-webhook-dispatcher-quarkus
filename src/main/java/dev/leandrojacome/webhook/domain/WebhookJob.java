package dev.leandrojacome.webhook.domain;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

public record WebhookJob(UUID id, URI destination, String payload, int attempts, Instant nextAttemptAt, WebhookStatus status) {
    public WebhookJob {
        if (!destination.getScheme().equals("https")) throw new IllegalArgumentException("destination must use HTTPS");
        if (payload == null || payload.isBlank()) throw new IllegalArgumentException("payload is required");
    }
    public static WebhookJob pending(URI destination, String payload, Instant now) {
        return new WebhookJob(UUID.randomUUID(), destination, payload, 0, now, WebhookStatus.PENDING);
    }
    public WebhookJob delivered() { return new WebhookJob(id, destination, payload, attempts + 1, nextAttemptAt, WebhookStatus.DELIVERED); }
    public WebhookJob retryAt(Instant when) { return new WebhookJob(id, destination, payload, attempts + 1, when, WebhookStatus.PENDING); }
    public WebhookJob exhausted() { return new WebhookJob(id, destination, payload, attempts + 1, nextAttemptAt, WebhookStatus.EXHAUSTED); }
}
