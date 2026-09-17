# 02 — API: Playlist Musical

## Objetivo
Construir uma API RESTful em Spring Boot para gerenciar **musicas**,
seguindo a mesma estrutura do projeto de referência (To-Do List).

## Stack (igual ao projeto de referência)
- Spring Boot 4.1.1, Java 21, Maven
- Dependências: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `mysql-connector-j`, `spring-boot-devtools`,
  `springdoc-openapi-starter-webmvc-ui`

## Estrutura de pastas esperada
```
src/main/java/com/playlist/api/
  controller/
    MusicaController.java
  dto/
    MusicaDTO.java
  mapper/
    MusicaMapper.java
  model/
    Musica.java
  repository/
    MusicaRepository.java
  service/
    MusicaService.java
src/main/resources/
  application.properties
```

## Entidade: `Musica`

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | gerado automaticamente (IDENTITY) |
| `titulo` | String | obrigatório, máx. 150 caracteres |
| `artista` | String | obrigatório, máx. 100 caracteres |
| `album` | String | opcional, máx. 100 caracteres |
| `duracaoSegundos` | Integer | opcional, não pode ser negativo |
| `favorita` | Boolean | default false |

## DTO — validações (Bean Validation)
- `titulo`: obrigatório, máx. 150 caracteres
- `artista`: obrigatório, máx. 100 caracteres
- `album`: opcional, máx. 100 caracteres
- `duracaoSegundos`: opcional, não pode ser negativo

## Endpoints esperados

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/musicas` | Lista todos os musicas |
| GET | `/api/musicas/{id}` | Busca um musica por id |
| POST | `/api/musicas` | Cria um novo musica |
| PUT | `/api/musicas/{id}` | Atualiza um musica existente |
| DELETE | `/api/musicas/{id}` | Remove um musica |
| PATCH | `/api/musicas/{id}/favoritar` | Ação especial: alterna o valor de favorita (true/false) |

## Exemplo de requisição POST
```json
{
  "titulo": "valor",
  "artista": "valor",
  "album": "valor",
  "duracaoSegundos": 123
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
