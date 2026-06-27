# Guia de Execucao

## Requisitos

- Docker
- Docker Compose

Nao e necessario instalar PostgreSQL ou Redmine manualmente.

## Variaveis de Ambiente

O arquivo `.env` define a conexao do backend com o Redmine criado pelo Docker Compose:

```env
REDMINE_URL=http://redmine:3000
REDMINE_API_KEY=0123456789abcdef0123456789abcdef01234567
```

## Subir o Ambiente

```bash
docker compose up --build
```

Servicos iniciados:

- `postgres`: banco PostgreSQL do backend.
- `redmine-postgres`: banco PostgreSQL do Redmine.
- `redmine`: instancia local do Redmine.
- `redmine-seed`: cria dados de demonstracao.
- `backend`: API Spring Boot.
- `frontend`: aplicacao React com Vite.

## Acessar o Redmine

```text
URL: http://localhost:3000
usuario: admin
senha: admin123
```

## Acessar o Backend

```text
URL: http://localhost:8080
```

## Acessar o Frontend

```text
URL: http://localhost:5173
```

## Testar a API

```bash
curl http://localhost:8080/health
curl http://localhost:8080/tasks
```

## Testar pela Interface

```text
1. Abra http://localhost:5173
2. Clique em Validar para confirmar a URL/API key do Redmine.
3. Clique em Iniciar em uma tarefa.
4. Use Pausar, Retomar e Finalizar.
5. Confira o apontamento em Historico.
```

## Recriar o Ambiente do Zero

Use este comando quando quiser apagar bancos e volumes:

```bash
docker compose down -v
docker compose up --build
```

## Dados Criados no Redmine

Projeto:

```text
TCC Demo - Redmine Timer
```

Tarefas:

- Implementar tela de listagem de tarefas.
- Criar fluxo de iniciar e pausar timer.
- Enviar apontamento de horas para o Redmine.
- Montar historico de apontamentos.
