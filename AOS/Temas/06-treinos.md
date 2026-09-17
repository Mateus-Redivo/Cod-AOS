# 06 — API: Diário de Treinos

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **treinos**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/treinos/api/
  controller/
    TreinoController.java
  dto/
    TreinoDTO.java
  mapper/
    TreinoMapper.java
  model/
    Treino.java
  repository/
    TreinoRepository.java
  service/
    TreinoService.java
src/main/resources/
  application.properties
```

## Entidade: `Treino`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `exercicio` | String | obrigatório, máx. 100 caracteres |
| `series` | Integer | obrigatório, não pode ser negativo |
| `repeticoes` | Integer | obrigatório, não pode ser negativo |
| `grupoMuscular` | String | opcional, máx. 50 caracteres |
| `feitoHoje` | Boolean | default false |

## DTO — validações (Bean Validation)
- `exercicio`: obrigatório, máx. 100 caracteres
- `series`: obrigatório, não pode ser negativo
- `repeticoes`: obrigatório, não pode ser negativo
- `grupoMuscular`: opcional, máx. 50 caracteres

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/treinos` | Lista todos os treinos |
| GET | `/api/treinos/{id}` | Busca um treino por id |
| POST | `/api/treinos` | Cria um novo treino |
| PUT | `/api/treinos/{id}` | Atualiza um treino existente |
| DELETE | `/api/treinos/{id}` | Remove um treino |
| PATCH | `/api/treinos/{id}/concluir` | Ação especial: marca o treino como concluído hoje (feitoHoje = true) |

## Exemplo de requisição POST
```json
{
  "exercicio": "valor",
  "series": 123,
  "repeticoes": 123,
  "grupoMuscular": "valor"
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
