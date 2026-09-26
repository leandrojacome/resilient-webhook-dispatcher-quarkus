package dev.leandrojacome.webhook.application;

import dev.leandrojacome.webhook.application.ports.DeliveryClient;
import dev.leandrojacome.webhook.application.ports.RetryStrategy;
import dev.leandrojacome.webhook.application.ports.WebhookRepository;
import java.time.Clock;

public final class DispatchDueWebhooks {
    private final WebhookRepository repository;
    private final DeliveryClient delivery;
    private final RetryStrategy retries;
    private final Clock clock;
    public DispatchDueWebhooks(WebhookRepository repository, DeliveryClient delivery, RetryStrategy retries, Clock clock) {
        this.repository = repository; this.delivery = delivery; this.retries = retries; this.clock = clock;
    }
    public int execute() {
        var now = clock.instant();
        var jobs = repository.findPendingDueAt(now);
        jobs.forEach(job -> {
            if (delivery.deliver(job)) repository.save(job.delivered());
            else if (retries.canRetry(job.attempts() + 1)) repository.save(job.retryAt(now.plus(retries.delayAfter(job.attempts() + 1))));
            else repository.save(job.exhausted());
        });
        return jobs.size();
    }
}
