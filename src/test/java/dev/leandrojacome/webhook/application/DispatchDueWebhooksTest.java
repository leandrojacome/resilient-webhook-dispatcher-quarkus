package dev.leandrojacome.webhook.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import dev.leandrojacome.webhook.adapters.ExponentialBackoffStrategy;
import dev.leandrojacome.webhook.adapters.InMemoryWebhookRepository;
import dev.leandrojacome.webhook.domain.WebhookJob;
import dev.leandrojacome.webhook.domain.WebhookStatus;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class DispatchDueWebhooksTest {
    private final Instant now = Instant.parse("2026-01-01T12:00:00Z");
    private final Clock clock = Clock.fixed(now, ZoneOffset.UTC);

    @Test void marks_successful_delivery() {
        var repository = new InMemoryWebhookRepository();
        var job = WebhookJob.pending(URI.create("https://example.test/hook"), "{}", now);
        repository.save(job);
        var useCase = new DispatchDueWebhooks(repository, ignored -> true, new ExponentialBackoffStrategy(3, Duration.ofSeconds(5)), clock);
        assertEquals(1, useCase.execute());
        assertTrue(repository.findPendingDueAt(now).isEmpty());
    }

    @Test void schedules_failed_delivery_using_retry_strategy() {
        var repository = new InMemoryWebhookRepository();
        repository.save(WebhookJob.pending(URI.create("https://example.test/hook"), "{}", now));
        var useCase = new DispatchDueWebhooks(repository, ignored -> false, new ExponentialBackoffStrategy(3, Duration.ofSeconds(5)), clock);
        useCase.execute();
        assertTrue(repository.findPendingDueAt(now).isEmpty());
        var scheduled = repository.findPendingDueAt(now.plusSeconds(5));
        assertEquals(1, scheduled.size());
        assertEquals(1, scheduled.getFirst().attempts());
        assertEquals(WebhookStatus.PENDING, scheduled.getFirst().status());
    }

    @Test void exhausts_after_maximum_attempts() {
        var repository = new InMemoryWebhookRepository();
        var job = new WebhookJob(java.util.UUID.randomUUID(), URI.create("https://example.test/hook"), "{}", 2, now, WebhookStatus.PENDING);
        repository.save(job);
        var useCase = new DispatchDueWebhooks(repository, ignored -> false, new ExponentialBackoffStrategy(3, Duration.ofSeconds(5)), clock);
        useCase.execute();
        assertTrue(repository.findPendingDueAt(now.plus(Duration.ofDays(1))).isEmpty());
    }
}
