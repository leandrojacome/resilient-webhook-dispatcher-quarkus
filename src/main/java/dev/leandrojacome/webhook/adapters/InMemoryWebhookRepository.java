package dev.leandrojacome.webhook.adapters;

import dev.leandrojacome.webhook.application.ports.WebhookRepository;
import dev.leandrojacome.webhook.domain.WebhookJob;
import dev.leandrojacome.webhook.domain.WebhookStatus;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class InMemoryWebhookRepository implements WebhookRepository {
    private final ConcurrentHashMap<UUID, WebhookJob> jobs = new ConcurrentHashMap<>();
    public void save(WebhookJob job) { jobs.put(job.id(), job); }
    public List<WebhookJob> findPendingDueAt(Instant instant) {
        return jobs.values().stream().filter(job -> job.status() == WebhookStatus.PENDING && !job.nextAttemptAt().isAfter(instant)).toList();
    }
}
