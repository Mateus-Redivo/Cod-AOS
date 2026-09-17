# 10 — API: Adoção de Animais

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **animals**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/adocao/api/
  controller/
    AnimalController.java
  dto/
    AnimalDTO.java
  mapper/
    AnimalMapper.java
  model/
    Animal.java
  repository/
    AnimalRepository.java
  service/
    AnimalService.java
src/main/resources/
  application.properties
```

## Entidade: `Animal`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `nome` | String | obrigatório, máx. 100 caracteres |
| `especie` | String | obrigatório, máx. 50 caracteres |
| `idade` | Integer | opcional, não pode ser negativo |
| `porte` | String | opcional, ex.: pequeno, médio, grande |
| `adotado` | Boolean | default false |

## DTO — validações (Bean Validation)
- `nome`: obrigatório, máx. 100 caracteres
- `especie`: obrigatório, máx. 50 caracteres
- `idade`: opcional, não pode ser negativo
- `porte`: opcional, ex.: pequeno, médio, grande

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/animais` | Lista todos os animais |
| GET | `/api/animais/{id}` | Busca um animal por id |
| POST | `/api/animais` | Cria um novo animal |
| PUT | `/api/animais/{id}` | Atualiza um animal existente |
| DELETE | `/api/animais/{id}` | Remove um animal |
| PATCH | `/api/animais/{id}/marcar-adotado` | Ação especial: marca o animal como adotado (adotado = true) |

## Exemplo de requisição POST
```json
{
  "nome": "valor",
  "especie": "valor",
  "idade": 123,
  "porte": "valor"
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
