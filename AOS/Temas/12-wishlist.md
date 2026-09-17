# 12 — API: Lista de Desejos

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **items**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/wishlist/api/
  controller/
    ItemController.java
  dto/
    ItemDTO.java
  mapper/
    ItemMapper.java
  model/
    Item.java
  repository/
    ItemRepository.java
  service/
    ItemService.java
src/main/resources/
  application.properties
```

## Entidade: `Item`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `nome` | String | obrigatório, máx. 150 caracteres |
| `preco` | BigDecimal/Double | obrigatório, não pode ser negativo |
| `prioridade` | String | opcional, ex.: baixa, média, alta |
| `comprado` | Boolean | default false |

## DTO — validações (Bean Validation)
- `nome`: obrigatório, máx. 150 caracteres
- `preco`: obrigatório, não pode ser negativo
- `prioridade`: opcional, ex.: baixa, média, alta

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/itens` | Lista todos os itens |
| GET | `/api/itens/{id}` | Busca um item por id |
| POST | `/api/itens` | Cria um novo item |
| PUT | `/api/itens/{id}` | Atualiza um item existente |
| DELETE | `/api/itens/{id}` | Remove um item |
| PATCH | `/api/itens/{id}/marcar-comprado` | Ação especial: marca o item como comprado (comprado = true) |

## Exemplo de requisição POST
```json
{
  "nome": "valor",
  "preco": 0.0,
  "prioridade": "valor"
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
