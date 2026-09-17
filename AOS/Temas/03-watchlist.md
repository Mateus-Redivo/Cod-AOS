# 03 — API: Watchlist de Filmes e Séries

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **titulos**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/watchlist/api/
  controller/
    TituloController.java
  dto/
    TituloDTO.java
  mapper/
    TituloMapper.java
  model/
    Titulo.java
  repository/
    TituloRepository.java
  service/
    TituloService.java
src/main/resources/
  application.properties
```

## Entidade: `Titulo`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `titulo` | String | obrigatório, máx. 150 caracteres |
| `genero` | String | opcional, máx. 50 caracteres |
| `plataforma` | String | opcional, máx. 50 caracteres |
| `nota` | Integer | opcional, de 0 a 10 |
| `assistido` | Boolean | default false |

## DTO — validações (Bean Validation)
- `titulo`: obrigatório, máx. 150 caracteres
- `genero`: opcional, máx. 50 caracteres
- `plataforma`: opcional, máx. 50 caracteres
- `nota`: opcional, de 0 a 10

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/titulos` | Lista todos os titulos |
| GET | `/api/titulos/{id}` | Busca um titulo por id |
| POST | `/api/titulos` | Cria um novo titulo |
| PUT | `/api/titulos/{id}` | Atualiza um titulo existente |
| DELETE | `/api/titulos/{id}` | Remove um titulo |
| PATCH | `/api/titulos/{id}/marcar-assistido` | Ação especial: marca o título como assistido (assistido = true) |

## Exemplo de requisição POST
```json
{
  "titulo": "valor",
  "genero": "valor",
  "plataforma": "valor",
  "nota": 123
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
