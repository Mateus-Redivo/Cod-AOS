# 08 — API: Cuidado com Plantas

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **plantas**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/plantas/api/
  controller/
    PlantaController.java
  dto/
    PlantaDTO.java
  mapper/
    PlantaMapper.java
  model/
    Planta.java
  repository/
    PlantaRepository.java
  service/
    PlantaService.java
src/main/resources/
  application.properties
```

## Entidade: `Planta`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `nome` | String | obrigatório, máx. 100 caracteres |
| `especie` | String | opcional, máx. 100 caracteres |
| `frequenciaRegaDias` | Integer | obrigatório, não pode ser negativo |
| `ultimaRega` | String (data) | opcional, formato AAAA-MM-DD |
| `saudavel` | Boolean | default true |

## DTO — validações (Bean Validation)
- `nome`: obrigatório, máx. 100 caracteres
- `especie`: opcional, máx. 100 caracteres
- `frequenciaRegaDias`: obrigatório, não pode ser negativo
- `ultimaRega`: opcional, formato AAAA-MM-DD

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/plantas` | Lista todos os plantas |
| GET | `/api/plantas/{id}` | Busca um planta por id |
| POST | `/api/plantas` | Cria um novo planta |
| PUT | `/api/plantas/{id}` | Atualiza um planta existente |
| DELETE | `/api/plantas/{id}` | Remove um planta |
| PATCH | `/api/plantas/{id}/regar` | Ação especial: atualiza ultimaRega para a data atual |

## Exemplo de requisição POST
```json
{
  "nome": "valor",
  "especie": "valor",
  "frequenciaRegaDias": 123,
  "ultimaRega": "valor"
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
