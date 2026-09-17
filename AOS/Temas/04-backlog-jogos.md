# 04 — API: Backlog de Jogos

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **jogos**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/backlogjogos/api/
  controller/
    JogoController.java
  dto/
    JogoDTO.java
  mapper/
    JogoMapper.java
  model/
    Jogo.java
  repository/
    JogoRepository.java
  service/
    JogoService.java
src/main/resources/
  application.properties
```

## Entidade: `Jogo`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `titulo` | String | obrigatório, máx. 150 caracteres |
| `plataforma` | String | opcional, máx. 50 caracteres |
| `genero` | String | opcional, máx. 50 caracteres |
| `horasJogadas` | Integer | opcional, não pode ser negativo |
| `zerado` | Boolean | default false |

## DTO — validações (Bean Validation)
- `titulo`: obrigatório, máx. 150 caracteres
- `plataforma`: opcional, máx. 50 caracteres
- `genero`: opcional, máx. 50 caracteres
- `horasJogadas`: opcional, não pode ser negativo

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/jogos` | Lista todos os jogos |
| GET | `/api/jogos/{id}` | Busca um jogo por id |
| POST | `/api/jogos` | Cria um novo jogo |
| PUT | `/api/jogos/{id}` | Atualiza um jogo existente |
| DELETE | `/api/jogos/{id}` | Remove um jogo |
| PATCH | `/api/jogos/{id}/marcar-zerado` | Ação especial: marca o jogo como zerado (zerado = true) |

## Exemplo de requisição POST
```json
{
  "titulo": "valor",
  "plataforma": "valor",
  "genero": "valor",
  "horasJogadas": 123
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
