package dev.leandrojacome.webhook.application.ports;

import dev.leandrojacome.webhook.domain.WebhookJob;
import java.time.Instant;
import java.util.List;

public interface WebhookRepository {
    void save(WebhookJob job);
    List<WebhookJob> findPendingDueAt(Instant instant);
}
