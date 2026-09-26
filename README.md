# Resilient Webhook Dispatcher with Quarkus

An API that queues and delivers webhooks with exponential retries and a bounded attempt count. The core is plain Java; Quarkus composes dependencies and exposes the HTTP boundary.

## Architecture and patterns

- **Strategy:** `RetryStrategy` encapsulates retry policy; `ExponentialBackoffStrategy` is the current implementation.
- **Repository:** job persistence stays behind an application port.
- **Ports and Adapters:** HTTP client, in-memory repository, and REST resource stay outside the core.
- **SOLID:** use cases have a single responsibility and depend on small abstractions.
- **Pragmatic DDD:** the Webhook Delivery bounded context uses `WebhookJob` as its entity and expresses invariants and transitions in ubiquitous language.

## Run

```bash
mvn test
```

See [Architecture](docs/architecture.md) and [ADR-001](docs/adr/001-retry-policy.md).

## Trade-offs

The in-memory adapter keeps the demonstration self-contained but is not durable. Production requires a transactional outbox, leases across replicas, configurable timeouts, circuit breaking, observability, and stronger SSRF protection than HTTPS-only destinations.

A State hierarchy was rejected because three states and simple transitions are clearer inside the entity. Visitor was rejected because no heterogeneous job structure requires multiple operations.

CI runs tests and a Trivy dependency scan that blocks fixable HIGH or CRITICAL vulnerabilities.

## License

MIT
