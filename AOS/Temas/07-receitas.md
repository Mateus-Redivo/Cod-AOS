# 07 — API: Receitas Culinárias

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **receitas**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/receitas/api/
  controller/
    ReceitaController.java
  dto/
    ReceitaDTO.java
  mapper/
    ReceitaMapper.java
  model/
    Receita.java
  repository/
    ReceitaRepository.java
  service/
    ReceitaService.java
src/main/resources/
  application.properties
```

## Entidade: `Receita`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `nome` | String | obrigatório, máx. 100 caracteres |
| `ingredientes` | String | obrigatório, texto livre ou lista separada por vírgula |
| `tempoPreparoMin` | Integer | opcional, não pode ser negativo |
| `dificuldade` | String | opcional, ex.: fácil, médio, difícil |
| `favorita` | Boolean | default false |

## DTO — validações (Bean Validation)
- `nome`: obrigatório, máx. 100 caracteres
- `ingredientes`: obrigatório, texto livre ou lista separada por vírgula
- `tempoPreparoMin`: opcional, não pode ser negativo
- `dificuldade`: opcional, ex.: fácil, médio, difícil

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/receitas` | Lista todos os receitas |
| GET | `/api/receitas/{id}` | Busca um receita por id |
| POST | `/api/receitas` | Cria um novo receita |
| PUT | `/api/receitas/{id}` | Atualiza um receita existente |
| DELETE | `/api/receitas/{id}` | Remove um receita |
| PATCH | `/api/receitas/{id}/favoritar` | Ação especial: alterna o valor de favorita (true/false) |

## Exemplo de requisição POST
```json
{
  "nome": "valor",
  "ingredientes": "valor",
  "tempoPreparoMin": 123,
  "dificuldade": "valor"
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
