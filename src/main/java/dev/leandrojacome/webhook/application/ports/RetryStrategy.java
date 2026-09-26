package dev.leandrojacome.webhook.application.ports;

import java.time.Duration;

public interface RetryStrategy {
    boolean canRetry(int completedAttempts);
    Duration delayAfter(int completedAttempts);
}
