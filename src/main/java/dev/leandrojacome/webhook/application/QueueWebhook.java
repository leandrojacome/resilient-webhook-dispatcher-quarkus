package dev.leandrojacome.webhook.application;

import dev.leandrojacome.webhook.application.ports.WebhookRepository;
import dev.leandrojacome.webhook.domain.WebhookJob;
import java.net.URI;
import java.time.Clock;

public final class QueueWebhook {
    private final WebhookRepository repository;
    private final Clock clock;
    public QueueWebhook(WebhookRepository repository, Clock clock) { this.repository = repository; this.clock = clock; }
    public WebhookJob execute(URI destination, String payload) {
        var job = WebhookJob.pending(destination, payload, clock.instant());
        repository.save(job);
        return job;
    }
}
