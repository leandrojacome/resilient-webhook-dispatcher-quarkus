package dev.leandrojacome.webhook.adapters;

import dev.leandrojacome.webhook.application.ports.RetryStrategy;
import java.time.Duration;

public final class ExponentialBackoffStrategy implements RetryStrategy {
    private final int maxAttempts;
    private final Duration baseDelay;
    public ExponentialBackoffStrategy(int maxAttempts, Duration baseDelay) { this.maxAttempts = maxAttempts; this.baseDelay = baseDelay; }
    public boolean canRetry(int completedAttempts) { return completedAttempts < maxAttempts; }
    public Duration delayAfter(int completedAttempts) { return baseDelay.multipliedBy(1L << Math.max(0, completedAttempts - 1)); }
}
