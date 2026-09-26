package dev.leandrojacome.webhook.application.ports;

import dev.leandrojacome.webhook.domain.WebhookJob;

public interface DeliveryClient { boolean deliver(WebhookJob job); }
