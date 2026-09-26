# Resilient Webhook Dispatcher — Quarkus

API para enfileirar e entregar webhooks com retentativas exponenciais e limite de tentativas. O núcleo é Java puro; Quarkus compõe dependências e expõe a borda HTTP.

## Arquitetura e padrões

- **Strategy (GoF):** `RetryStrategy` encapsula a política de retentativa; `ExponentialBackoffStrategy` é a implementação atual.
- **Repository:** `WebhookRepository` separa os casos de uso da persistência.
- **Ports and Adapters:** cliente HTTP, repositório em memória e recurso REST ficam fora do núcleo.
- **SOLID:** casos de uso possuem responsabilidade única e dependem de abstrações pequenas.
- **DDD proporcional:** bounded context de Entrega de Webhooks, entidade `WebhookJob`, invariantes e transições expressas na linguagem ubíqua.

```bash
mvn verify
mvn quarkus:dev
```

Veja [Arquitetura](docs/architecture.md) e [ADR-001](docs/adr/001-retry-policy.md).

## Trade-offs

O adaptador em memória torna a demonstração autocontida, mas não é durável. Produção exigiria outbox transacional, leases para concorrência entre réplicas, timeout configurável, circuit breaker, observabilidade e política contra SSRF mais completa que a exigência de HTTPS.

Uma hierarquia State foi descartada porque três estados e transições simples ficam mais claros na entidade; Visitor foi descartado por não haver estrutura heterogênea com múltiplas operações.

O CI executa testes e auditoria de dependências com Trivy, bloqueando vulnerabilidades corrigíveis HIGH/CRITICAL.

## Licença

MIT.
