# 05 — API: Rastreador de Hábitos

## Objetivo

Construir uma API RESTful em Spring Boot para gerenciar **habitos**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)

- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada

```
src/main/java/com/habitos/api/
  controller/
    HabitoController.java
  dto/
    HabitoDTO.java
  mapper/
    HabitoMapper.java
  model/
    Habito.java
  repository/
    HabitoRepository.java
  service/
    HabitoService.java
src/main/resources/
  application.properties
```

## Entidade: `Habito`

| Campo | Tipo | Regra |
| --- | --- | --- |
| `id` | Long | gerado automaticamente (IDENTITY) |
| `nome` | String | obrigatório, máx. 100 caracteres |
| `frequencia` | String | opcional, ex.: diária, semanal |
| `streakDias` | Integer | opcional, não pode ser negativo, default 0 |
| `descricao` | String | opcional, máx. 300 caracteres |
| `feitoHoje` | Boolean | default false |

## DTO — validações (Bean Validation)

- `nome`: obrigatório, máx. 100 caracteres
- `frequencia`: opcional, ex.: diária, semanal
- `streakDias`: opcional, não pode ser negativo, default 0
- `descricao`: opcional, máx. 300 caracteres

## Endpoints esperados

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/api/habitos` | Lista todos os habitos |
| GET | `/api/habitos/{id}` | Busca um habito por id |
| POST | `/api/habitos` | Cria um novo habito |
| PUT | `/api/habitos/{id}` | Atualiza um habito existente |
| DELETE | `/api/habitos/{id}` | Remove um habito |
| PATCH | `/api/habitos/{id}/concluir-hoje` | Ação especial: marca feitoHoje = true e incrementa streakDias em 1 |

## Exemplo de requisição POST

```json
{
  "nome": "valor",
  "frequencia": "valor",
  "streakDias": 123,
  "descricao": "valor"
}
```

## Documentação

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/api-docs`

## Checklist de entrega

- [ ] Model/Entity com anotações JPA
- [ ] DTO com validações Bean Validation
- [ ] Mapper (Entity ↔ DTO)
- [ ] Repository (`JpaRepository`)
- [ ] Service com as regras de negócio
- [ ] Controller com os 6 endpoints da tabela acima
- [ ] Testado via Swagger, Postman ou Insomnia
