# 09 — API: Controle de Gastos Pessoais

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **despesas**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/gastos/api/
  controller/
    DespesaController.java
  dto/
    DespesaDTO.java
  mapper/
    DespesaMapper.java
  model/
    Despesa.java
  repository/
    DespesaRepository.java
  service/
    DespesaService.java
src/main/resources/
  application.properties
```

## Entidade: `Despesa`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `descricao` | String | obrigatório, máx. 150 caracteres |
| `valor` | BigDecimal/Double | obrigatório, maior que zero |
| `categoria` | String | opcional, máx. 50 caracteres |
| `data` | String (data) | opcional, formato AAAA-MM-DD |
| `paga` | Boolean | default false |

## DTO — validações (Bean Validation)
- `descricao`: obrigatório, máx. 150 caracteres
- `valor`: obrigatório, maior que zero
- `categoria`: opcional, máx. 50 caracteres
- `data`: opcional, formato AAAA-MM-DD

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/despesas` | Lista todos os despesas |
| GET | `/api/despesas/{id}` | Busca um despesa por id |
| POST | `/api/despesas` | Cria um novo despesa |
| PUT | `/api/despesas/{id}` | Atualiza um despesa existente |
| DELETE | `/api/despesas/{id}` | Remove um despesa |
| PATCH | `/api/despesas/{id}/marcar-paga` | Ação especial: marca a despesa como paga (paga = true) |

## Exemplo de requisição POST
```json
{
  "descricao": "valor",
  "valor": 0.0,
  "categoria": "valor",
  "data": "valor"
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
