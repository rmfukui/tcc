# Redmine Timer

Sistema MVP para gerenciamento de log de horas integrado ao Redmine.

Este projeto faz parte de um TCC de Engenharia de Software e tem como objetivo demonstrar uma aplicacao funcional, simples e bem estruturada para listar tarefas do Redmine, controlar timers e registrar apontamentos de horas.

## Status da Entrega

Funcionalidades implementadas nesta primeira etapa:

- Backend Spring Boot estruturado em camadas.
- PostgreSQL configurado para a aplicacao.
- Redmine configurado via Docker Compose para demonstracao.
- Seed automatico do Redmine com projeto, usuario, API key e tarefas de teste.
- Integracao inicial com Redmine usando WebClient.
- Endpoint `GET /health`.
- Endpoint `GET /tasks` funcional.
- Endpoint `POST /auth/validate`.
- Endpoints de timer para iniciar, pausar, retomar e finalizar.
- Registro automatico de horas no Redmine ao finalizar timer.
- Endpoint `GET /history`.
- Frontend React com Material UI para demonstracao do MVP.
- Tratamento centralizado de excecoes.
- DTOs com records.
- Entidades JPA principais do MVP.

## Stack

Backend:

- Java 21
- Spring Boot 3
- Maven
- Spring Web
- Spring Data JPA
- PostgreSQL
- Lombok
- Validation
- WebClient

Infra:

- Docker
- Docker Compose
- Redmine 5.1 para ambiente de demonstracao

Frontend:

- React
- Vite
- Axios
- Material UI

## Estrutura de Pastas

```text
redmine-timer
|-- docker
|   `-- redmine
|       |-- database.yml
|       `-- seed.rb
|-- docs
|   |-- api.md
|   |-- arquitetura.md
|   `-- execucao.md
|-- frontend
|   |-- src
|   |   |-- api
|   |   |-- components
|   |   |-- utils
|   |   |-- App.jsx
|   |   |-- main.jsx
|   |   `-- styles.css
|   |-- Dockerfile
|   |-- index.html
|   |-- package.json
|   `-- vite.config.js
|-- src
|   `-- main
|       |-- java
|       |   `-- com
|       |       `-- renato
|       |           `-- redminetimer
|       |               |-- config
|       |               |-- controller
|       |               |-- dto
|       |               |-- entity
|       |               |-- exception
|       |               |-- integration
|       |               |-- mapper
|       |               |-- repository
|       |               |-- service
|       |               `-- util
|       `-- resources
|           `-- application.yml
|-- Dockerfile
|-- docker-compose.yml
|-- pom.xml
`-- README.md
```

## Arquitetura

O backend utiliza uma arquitetura em camadas simples:

- `controller`: expoe endpoints REST.
- `service`: concentra regras de aplicacao.
- `integration`: isola comunicacao com o Redmine.
- `repository`: acessa dados com Spring Data JPA.
- `entity`: representa entidades persistidas.
- `dto`: define contratos de entrada e saida.
- `mapper`: converte objetos externos para respostas da API.
- `config`: centraliza configuracoes.
- `exception`: padroniza erros da API.
- `util`: agrupa utilitarios simples.

Essa divisao reduz acoplamento, facilita testes e permite explicar o projeto no TCC sem recorrer a uma arquitetura corporativa pesada.

O frontend utiliza uma organizacao objetiva:

- `api`: chamadas HTTP com Axios.
- `components`: blocos visuais reutilizaveis.
- `utils`: funcoes de formatacao.
- `App.jsx`: composicao do fluxo principal do MVP.

## Como Executar

O arquivo `.env` ja esta configurado para o Redmine local do Docker Compose:

```env
REDMINE_URL=http://redmine:3000
REDMINE_API_KEY=0123456789abcdef0123456789abcdef01234567
```

Suba o ambiente:

```bash
docker compose up --build
```

Na primeira execucao, o Redmine pode levar alguns minutos para preparar o banco.

## Acessos

Backend:

```text
http://localhost:8080
```

Frontend:

```text
http://localhost:5173
```

Redmine:

```text
http://localhost:3000
usuario: admin
senha: admin123
```

## Endpoints

Health check:

```http
GET http://localhost:8080/health
```

Listar tarefas atribuidas ao usuario autenticado pela API key:

```http
GET http://localhost:8080/tasks
```

Validar configuracao do Redmine:

```http
POST http://localhost:8080/auth/validate
```

Timer:

```http
POST http://localhost:8080/timer/start
POST http://localhost:8080/timer/pause
POST http://localhost:8080/timer/resume
POST http://localhost:8080/timer/finish
```

Historico:

```http
GET http://localhost:8080/history
```

## Testes Manuais

```bash
curl http://localhost:8080/health
curl http://localhost:8080/tasks
```

O endpoint `/tasks` deve retornar as tarefas criadas no seed do Redmine.

Fluxo pela interface:

```text
1. Acesse http://localhost:5173
2. Clique em Validar
3. Escolha uma tarefa e clique em Iniciar
4. Pause, retome ou finalize o timer
5. Confira o historico
```

## Documentacao

- [Arquitetura](docs/arquitetura.md)
- [Execucao](docs/execucao.md)
- [API](docs/api.md)
