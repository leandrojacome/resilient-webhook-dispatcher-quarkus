# Architecture

## Domain model

The **Webhook Delivery** bounded context models `WebhookJob` as a UUID-identified entity. HTTPS destination, payload, attempts, next execution, and status compose its state. Invariants reject non-HTTPS destinations and empty payloads. Ubiquitous terms include queue, deliver, attempt, pending, delivered, and exhausted.

## Layers

- **Domain:** entity, states, and transitions without Quarkus.
- **Application:** explicit `QueueWebhook` and `DispatchDueWebhooks` use cases.
- **Infrastructure:** REST, CDI, in-memory repository, and JDK HTTP Client.

Flow: find due jobs -> deliver -> mark successful, schedule another attempt, or exhaust.

## Patterns and alternatives

- **Strategy:** `RetryStrategy` separates backoff from orchestration. Fixed numbers inside the use case were rejected.
- **Repository:** allows memory to be replaced by a transactional outbox.
- **Adapter:** inbound and outbound HTTP stay at the boundaries.
- A formal State pattern was rejected because an enum with entity-owned transitions keeps three phases readable. Visitor does not apply because there is no job hierarchy requiring independent operations.

The in-memory repository simplifies local execution but offers no durability or coordination. Production needs an outbox, leases, timeouts, circuit breaking, jitter, and complete SSRF defenses.
