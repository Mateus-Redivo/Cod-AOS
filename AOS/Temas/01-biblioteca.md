# 01 — API: Biblioteca Pessoal

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **livros**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/biblioteca/api/
  controller/
    LivroController.java
  dto/
    LivroDTO.java
  mapper/
    LivroMapper.java
  model/
    Livro.java
  repository/
    LivroRepository.java
  service/
    LivroService.java
src/main/resources/
  application.properties
```

## Entidade: `Livro`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `titulo` | String | obrigatório, máx. 150 caracteres |
| `autor` | String | obrigatório, máx. 100 caracteres |
| `genero` | String | opcional, máx. 50 caracteres |
| `paginas` | Integer | opcional, não pode ser negativo |
| `lido` | Boolean | default false |

## DTO — validações (Bean Validation)
- `titulo`: obrigatório, máx. 150 caracteres
- `autor`: obrigatório, máx. 100 caracteres
- `genero`: opcional, máx. 50 caracteres
- `paginas`: opcional, não pode ser negativo

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/livros` | Lista todos os livros |
| GET | `/api/livros/{id}` | Busca um livro por id |
| POST | `/api/livros` | Cria um novo livro |
| PUT | `/api/livros/{id}` | Atualiza um livro existente |
| DELETE | `/api/livros/{id}` | Remove um livro |
| PATCH | `/api/livros/{id}/marcar-lido` | Ação especial: marca o livro como lido (lido = true) |

## Exemplo de requisição POST
```json
{
  "titulo": "valor",
  "autor": "valor",
  "genero": "valor",
  "paginas": 123
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
