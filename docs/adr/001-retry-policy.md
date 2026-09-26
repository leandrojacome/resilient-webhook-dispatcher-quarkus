# ADR-001: Bounded exponential retry

## Status

Accepted for the portfolio scope.

## Decision

Apply exponential backoff and stop after five attempts.

## Consequences

This reduces pressure on unavailable destinations and prevents infinite retries. Jitter and circuit breaking remain future evolutions to avoid synchronized clients and cascading failures.
