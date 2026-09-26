# ADR-001: retentativa exponencial com limite

## Status

Aceito.

## Decisão

Aplicar backoff exponencial e encerrar após cinco tentativas.

## Consequências

Reduz pressão sobre destinos indisponíveis e impede retries infinitos. Jitter e circuit breaker foram deixados como evolução para evitar sincronização de clientes e falhas em cascata.
