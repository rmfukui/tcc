# API do Backend

## Base URL

```text
http://localhost:8080
```

## Health Check

Verifica se o backend esta em execucao.

```http
GET /health
```

Resposta esperada:

```json
{
  "status": "UP",
  "application": "redmine-timer",
  "timestamp": "2026-05-22T21:42:29.69461078"
}
```

## Listar Tarefas

Lista tarefas atribuidas ao usuario autenticado pela API key configurada.

```http
GET /tasks
```

Fluxo interno:

```text
TaskController -> TaskService -> RedmineClient -> Redmine API
```

Endpoint usado no Redmine:

```http
GET /issues.json?assigned_to_id=me
```

Resposta esperada:

```json
[
  {
    "id": 4,
    "subject": "Montar historico de apontamentos",
    "projectName": "TCC Demo - Redmine Timer",
    "statusName": "New",
    "trackerName": "Feature"
  }
]
```

## Erros

## Validar Redmine

Valida a URL e a API key do Redmine. Se as credenciais forem validas, a configuracao e salva no banco local para as proximas chamadas.

```http
POST /auth/validate
Content-Type: application/json
```

Request:

```json
{
  "redmineUrl": "http://redmine:3000",
  "apiKey": "0123456789abcdef0123456789abcdef01234567"
}
```

Response:

```json
{
  "valid": true,
  "redmineUrl": "http://redmine:3000",
  "username": "Redmine Admin"
}
```

## Iniciar Timer

```http
POST /timer/start
Content-Type: application/json
```

Request:

```json
{
  "issueId": 1,
  "issueName": "Implementar tela de listagem de tarefas"
}
```

Response:

```json
{
  "id": 1,
  "issueId": 1,
  "issueName": "Implementar tela de listagem de tarefas",
  "startTime": "2026-05-22T21:50:00",
  "endTime": null,
  "elapsedSeconds": 0,
  "status": "RUNNING"
}
```

## Pausar Timer

```http
POST /timer/pause
```

Pausa o timer em execucao e acumula os segundos trabalhados.

## Retomar Timer

```http
POST /timer/resume
```

Retoma um timer pausado.

## Finalizar Timer

```http
POST /timer/finish
Content-Type: application/json
```

Request:

```json
{
  "comments": "Implementacao inicial da funcionalidade"
}
```

Ao finalizar, o backend calcula as horas, envia para o Redmine em `/time_entries.json` e grava o historico local.

## Historico

```http
GET /history
```

Response:

```json
[
  {
    "id": 1,
    "redmineEntryId": 10,
    "issueId": 1,
    "hours": 0.25,
    "comments": "Implementacao inicial da funcionalidade",
    "syncedAt": "2026-05-22T21:55:00"
  }
]
```

## Erros

Os erros sao padronizados pelo `GlobalExceptionHandler`.

Exemplo:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Configure REDMINE_URL e REDMINE_API_KEY antes de buscar tarefas.",
  "path": "/tasks",
  "timestamp": "2026-05-22T21:42:29"
}
```

## Endpoints Planejados

Estes endpoints fazem parte de melhorias futuras:

```http
GET /timer/current
DELETE /timer/current
```

## Consumo pelo Frontend

O frontend consome a API usando Axios. A URL base e configurada por:

```env
VITE_API_URL=http://localhost:8080
```

No Docker Compose, o navegador acessa o backend pela porta publicada em `localhost:8080`.
