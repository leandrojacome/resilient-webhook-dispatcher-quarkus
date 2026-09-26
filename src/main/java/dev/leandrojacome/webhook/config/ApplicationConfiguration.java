package dev.leandrojacome.webhook.config;

import dev.leandrojacome.webhook.application.DispatchDueWebhooks;
import dev.leandrojacome.webhook.application.QueueWebhook;
import dev.leandrojacome.webhook.application.ports.DeliveryClient;
import dev.leandrojacome.webhook.application.ports.WebhookRepository;
import dev.leandrojacome.webhook.adapters.ExponentialBackoffStrategy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.time.Clock;
import java.time.Duration;

@ApplicationScoped
public class ApplicationConfiguration {
    @Produces @ApplicationScoped Clock clock() { return Clock.systemUTC(); }
    @Produces @ApplicationScoped QueueWebhook queue(WebhookRepository repository, Clock clock) { return new QueueWebhook(repository, clock); }
    @Produces @ApplicationScoped DispatchDueWebhooks dispatch(WebhookRepository repository, DeliveryClient delivery, Clock clock) {
        return new DispatchDueWebhooks(repository, delivery, new ExponentialBackoffStrategy(5, Duration.ofSeconds(5)), clock);
    }
}
