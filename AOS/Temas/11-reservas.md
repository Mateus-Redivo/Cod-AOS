# 11 — API: Reserva de Salas e Equipamentos

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **reservas**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/reservas/api/
  controller/
    ReservaController.java
  dto/
    ReservaDTO.java
  mapper/
    ReservaMapper.java
  model/
    Reserva.java
  repository/
    ReservaRepository.java
  service/
    ReservaService.java
src/main/resources/
  application.properties
```

## Entidade: `Reserva`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `recurso` | String | obrigatório, máx. 100 caracteres (sala/equipamento) |
| `responsavel` | String | obrigatório, máx. 100 caracteres |
| `dataHora` | String (data/hora) | obrigatório, formato AAAA-MM-DDTHH:mm |
| `capacidade` | Integer | opcional, não pode ser negativo |
| `confirmada` | Boolean | default false |

## DTO — validações (Bean Validation)
- `recurso`: obrigatório, máx. 100 caracteres (sala/equipamento)
- `responsavel`: obrigatório, máx. 100 caracteres
- `dataHora`: obrigatório, formato AAAA-MM-DDTHH:mm
- `capacidade`: opcional, não pode ser negativo

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/reservas` | Lista todos os reservas |
| GET | `/api/reservas/{id}` | Busca um reserva por id |
| POST | `/api/reservas` | Cria um novo reserva |
| PUT | `/api/reservas/{id}` | Atualiza um reserva existente |
| DELETE | `/api/reservas/{id}` | Remove um reserva |
| PATCH | `/api/reservas/{id}/confirmar` | Ação especial: marca a reserva como confirmada (confirmada = true) |

## Exemplo de requisição POST
```json
{
  "recurso": "valor",
  "responsavel": "valor",
  "dataHora": "valor",
  "capacidade": 123
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
