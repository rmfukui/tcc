# Arquitetura do Backend

## Objetivo

O backend do Redmine Timer foi projetado para ser simples, funcional e adequado ao contexto de um TCC. A proposta e demonstrar uma aplicacao real integrada ao Redmine sem adicionar complexidade desnecessaria.

## Decisoes Arquiteturais

O projeto segue uma organizacao em camadas:

```text
com.renato.redminetimer
|-- config
|-- controller
|-- dto
|-- entity
|-- exception
|-- integration
|-- mapper
|-- repository
|-- service
`-- util
```

## Responsabilidades

`controller`

Recebe requisicoes HTTP, chama os servicos da aplicacao e retorna `ResponseEntity`.

`service`

Concentra regras de aplicacao. Exemplos:

- `TaskService`: coordena a busca de tarefas no Redmine.
- `AuthService`: valida e salva a configuracao do Redmine.
- `TimerService`: controla iniciar, pausar, retomar e finalizar timers.
- `HistoryService`: lista apontamentos sincronizados.

`integration`

Isola detalhes da API externa do Redmine. A classe `RedmineClient` usa `WebClient` para buscar tarefas em `/issues.json?assigned_to_id=me`.

Tambem registra horas no Redmine usando `/time_entries.json`.

`repository`

Contem interfaces Spring Data JPA para persistencia das entidades do sistema.

`entity`

Representa as tabelas principais do MVP:

- `UserConfig`
- `TimerSession`
- `TimeEntryLog`

`dto`

Define contratos da API. Records sao usados para respostas simples e imutaveis.

`mapper`

Evita que objetos da integracao externa sejam expostos diretamente nos controllers.

`exception`

Centraliza o tratamento de erros com `GlobalExceptionHandler`.

## Justificativa Academica

A arquitetura em camadas foi escolhida por equilibrar simplicidade e organizacao. Ela permite separar responsabilidades, reduzir acoplamento e facilitar manutencao, pontos importantes em projetos de Engenharia de Software.

Nao foram adotados microservicos, Kubernetes ou autenticacao complexa porque o objetivo do projeto e entregar um MVP demonstravel, e nao uma plataforma corporativa completa.

## Fluxo Principal do MVP

```text
1. Usuario valida URL e API key do Redmine.
2. Backend salva a configuracao localmente.
3. Usuario lista tarefas atribuidas.
4. Usuario inicia um timer para uma tarefa.
5. Usuario pausa ou retoma o timer quando necessario.
6. Usuario finaliza o timer.
7. Backend calcula as horas e envia para o Redmine.
8. Backend salva o historico local do apontamento.
```

## Frontend

O frontend foi criado com React, Vite, Axios e Material UI. A organizacao e simples:

```text
frontend/src
|-- api
|-- components
|-- utils
|-- App.jsx
|-- main.jsx
`-- styles.css
```

`api`

Centraliza chamadas HTTP para o backend.

`components`

Contem os componentes da interface:

- `ConnectionPanel`
- `TaskList`
- `TimerPanel`
- `HistoryTable`

`App.jsx`

Coordena o estado da tela, acionando os endpoints do backend e atualizando tarefas, timer e historico.
