# Arquitetura

## Contexto e linguagem

Bounded context **Entrega de Webhooks**. `WebhookJob` é a entidade identificada por UUID; destino HTTPS, payload, tentativas, próxima execução e status formam seu estado. As invariantes impedem destino sem HTTPS e payload vazio. Termos ubíquos: enfileirar, entregar, tentativa, pendente, entregue e esgotado.

## Fronteiras

- **Domínio:** entidade, estados e transições, sem Quarkus.
- **Aplicação:** casos de uso explícitos `QueueWebhook` e `DispatchDueWebhooks`.
- **Infraestrutura:** REST, CDI, repositório em memória e JDK HTTP Client.

Fluxo: buscar pendentes vencidos → entregar → marcar sucesso, agendar nova tentativa ou esgotar.

## Padrões e alternativas

- **Strategy:** `RetryStrategy` separa backoff da orquestração. Números fixos no caso de uso foram descartados.
- **Repository:** permite substituir memória por outbox transacional.
- **Adapter:** HTTP de entrada e saída ficam nas bordas.
- State formal foi descartado: o enum com transições na entidade mantém as três fases legíveis sem uma classe por estado. Visitor não se aplica porque operações sobre uma hierarquia de jobs não existem.

O repositório em memória facilita execução local, mas não oferece durabilidade ou coordenação. Produção requer outbox, lease, timeout, circuit breaker, jitter e defesa SSRF completa.
