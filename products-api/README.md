# Products API

Uma API REST de gerenciamento de produtos construída com Spring Boot e MySQL. Cobre o ciclo completo de CRUD — criação, listagem, atualização e remoção — com validação de dados, documentação automática via Swagger e suporte a Docker.

---

## O que você vai precisar

- **Java 21** (ou superior)
- **Maven** — o projeto já vem com o wrapper, então você pode usar `./mvnw` (ou `mvnw.cmd` no Windows)
- **MySQL 8** — ou **Docker + Docker Compose** para subir tudo de uma vez sem instalar o banco manualmente

---

## Passo 1 — Criar o projeto no Spring Initializr

Acesse [start.spring.io](https://start.spring.io) e configure assim:

| Campo | Valor |
| --- | --- |
| Project | Maven |
| Language | Java |
| Spring Boot | 4.0.6 |
| Group | `com.aos` |
| Artifact | `products-api` |
| Packaging | Jar |
| Java | 21 |

Adicione as seguintes dependências antes de gerar o projeto:

- **Spring Web** — para criar os endpoints REST
- **Spring Data JPA** — para trabalhar com o banco de dados de forma simplificada
- **Validation** — para validar os dados que chegam na API
- **MySQL Driver** — para conectar com o MySQL
- **Spring Boot DevTools** — reinicia a aplicação automaticamente durante o desenvolvimento
- **Lombok** — elimina código repetitivo de getters, setters e construtores (veja `README-LOMBOK.md`)

Clique em **Generate**, extraia o zip e abra a pasta no seu editor.

Além das dependências do Initializr, adicione manualmente no `pom.xml` o **springdoc-openapi** para gerar a documentação Swagger automaticamente:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.2</version>
</dependency>
```

> O Lombok também precisa ser configurado como annotation processor no plugin do Maven. Consulte o `pom.xml` do projeto para ver a configuração completa.

---

## Passo 2 — Estrutura de pastas

Organize o projeto dentro de `src/main/java/com/aos/productsapi/` da seguinte forma:

```text
productsapi/
├── config/
│   ├── CorsConfig.java
│   └── OpenApiConfig.java
├── controller/
│   └── ProductController.java
├── dto/
│   └── ProductDTO.java
├── exception/
│   └── ValidationExceptionHandler.java
├── mapper/
│   └── ProductMapper.java
├── model/
│   └── Product.java
├── repository/
│   └── ProductRepository.java
├── service/
│   └── ProductService.java
└── ProductsApiApplication.java
```

Cada pasta tem uma responsabilidade clara:

| Pasta | Responsabilidade |
| --- | --- |
| `model` | Representa as tabelas do banco de dados |
| `dto` | Define o que a API recebe e retorna (com validações) |
| `mapper` | Converte entre entidade e DTO |
| `repository` | Faz as operações no banco |
| `service` | Contém a lógica de negócio |
| `controller` | Recebe as requisições HTTP e chama o service |
| `config` | Configurações da aplicação (CORS, Swagger) |
| `exception` | Trata erros e devolve respostas legíveis |

---

## Passo 3 — Criar os arquivos

### `model/Product.java`

A entidade representa a tabela no banco. O JPA cria a tabela `product` automaticamente com esses campos:

```java
package com.aos.productsapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private int quantity;

    public Product() {}

    public Product(String name, String description, double value, int quantity) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.quantity = quantity;
    }

    // getters e setters...
}
```

---

### `dto/ProductDTO.java`

O DTO é o contrato da API — define o que ela aceita e o que ela devolve. Carrega as validações dos campos e as anotações do Swagger:

```java
package com.aos.productsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Product data transfer object")
public class ProductDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @Positive(message = "Value must be greater than zero")
    private double value;

    @PositiveOrZero(message = "Quantity must be zero or greater")
    private int quantity;

    // Construtor vazio necessário para o Jackson desserializar o JSON da requisição
    public ProductDTO() {}

    // Construtor usado pelo ProductMapper para montar o DTO a partir da entidade
    public ProductDTO(Long id, String name, String description, double value, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.quantity = quantity;
    }

    // getters e setters...
}
```

---

### `mapper/ProductMapper.java`

Converte entre entidade e DTO. O service não precisa saber como montar um do outro — essa responsabilidade fica aqui:

```java
package com.aos.productsapi.mapper;

import com.aos.productsapi.dto.ProductDTO;
import com.aos.productsapi.model.Product;

public class ProductMapper {

    private ProductMapper() {}

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getValue(),
            product.getQuantity()
        );
    }

    public static Product toEntity(ProductDTO dto) {
        return new Product(
            dto.getName(),
            dto.getDescription(),
            dto.getValue(),
            dto.getQuantity()
        );
    }
}
```

> `ProductMapper` é uma classe utilitária com métodos estáticos, igual às classes `Math` e `Collections` do Java. Por isso o construtor é `private` — ninguém precisa criar uma instância dela.

---

### `repository/ProductRepository.java`

Só estender o `JpaRepository` já é suficiente para ter todos os métodos CRUD prontos:

```java
package com.aos.productsapi.repository;

import com.aos.productsapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
```

---

### `service/ProductService.java`

O service é onde fica a lógica de negócio. Ele usa o repository para acessar o banco e o mapper para converter os dados:

```java
package com.aos.productsapi.service;

import com.aos.productsapi.dto.ProductDTO;
import com.aos.productsapi.mapper.ProductMapper;
import com.aos.productsapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(ProductMapper::toDTO)
            .toList();
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id).map(ProductMapper::toDTO);
    }

    public ProductDTO createProduct(ProductDTO dto) {
        return ProductMapper.toDTO(productRepository.save(ProductMapper.toEntity(dto)));
    }

    public Optional<ProductDTO> updateProduct(Long id, ProductDTO dto) {
        return productRepository.findById(id).map(product -> {
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setValue(dto.getValue());
            product.setQuantity(dto.getQuantity());
            return ProductMapper.toDTO(productRepository.save(product));
        });
    }

    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) return false;
        productRepository.deleteById(id);
        return true;
    }
}
```

---

### `controller/ProductController.java`

O controller mapeia cada rota HTTP para o método correspondente no service:

```java
package com.aos.productsapi.controller;

import com.aos.productsapi.dto.ProductDTO;
import com.aos.productsapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "CRUD operations for products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "List all products")
    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.status(201).body(productService.createProduct(dto));
    }

    @Operation(summary = "Update an existing product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productService.deleteProduct(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
```

---

### `exception/ValidationExceptionHandler.java`

Intercepta os erros de validação do `@Valid` e devolve uma resposta JSON limpa e legível em vez da mensagem de erro padrão do Spring:

```java
package com.aos.productsapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }
}
```

Exemplo de resposta quando os dados enviados são inválidos:

```json
{
  "name": "Name is required",
  "value": "Value must be greater than zero"
}
```

---

### `config/CorsConfig.java`

Define quais origens (domínios e portas) podem chamar a API. Necessário quando há um frontend rodando em outra porta:

```java
package com.aos.productsapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/products/**")
            .allowedOrigins("http://localhost:3000", "http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
```

---

### `config/OpenApiConfig.java`

Define o título e a descrição que aparecem na página do Swagger:

```java
package com.aos.productsapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Products API")
                .description("REST API for product management")
                .version("1.0.0"));
    }
}
```

---

## Passo 4 — Configurar o banco de dados

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.application.name=products-api

server.port=8081

spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:productsdb}?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:sua_senha_aqui}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

- `createDatabaseIfNotExist=true` — cria o banco `productsdb` automaticamente se não existir
- `ddl-auto=update` — o JPA cria e atualiza as tabelas conforme as entidades
- `show-sql=true` — exibe as queries SQL no console (útil para aprender o que o JPA está fazendo)
- Os valores `${VAR:padrão}` usam variáveis de ambiente, com um valor padrão caso a variável não esteja definida

---

## Passo 5 — Rodar a aplicação

### Opção A: localmente com MySQL instalado

Certifique-se de que o MySQL está rodando e que o usuário configurado tem permissão para criar bancos. Depois, na raiz do projeto:

```bash
# Linux / Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

### Opção B: com Docker Compose (recomendado)

Com Docker instalado, apenas rode:

```bash
docker compose up --build
```

Isso sobe dois containers: um com MySQL 8 e outro com a API. O container da API aguarda o banco ficar saudável antes de iniciar.

Para parar tudo:

```bash
docker compose down
```

Para parar e apagar os dados do banco também:

```bash
docker compose down -v
```

---

## Endpoints disponíveis

A API roda em `http://localhost:8081`. Todos os endpoints ficam sob `/products`.

| Método | Rota | O que faz | Resposta |
| --- | --- | --- | --- |
| GET | `/products` | Lista todos os produtos | 200 |
| GET | `/products/{id}` | Busca um produto pelo ID | 200 / 404 |
| POST | `/products` | Cria um novo produto | 201 |
| PUT | `/products/{id}` | Atualiza um produto existente | 200 / 404 |
| DELETE | `/products/{id}` | Remove um produto | 204 / 404 |

### Exemplo de corpo para POST e PUT

```json
{
  "name": "Notebook",
  "description": "Notebook 15 polegadas, 16GB RAM",
  "value": 3999.99,
  "quantity": 10
}
```

### Códigos de resposta

| Código | Significado |
| --- | --- |
| 200 | Sucesso |
| 201 | Produto criado |
| 204 | Produto deletado (sem corpo na resposta) |
| 400 | Dados inválidos — a resposta indica qual campo está errado |
| 404 | Produto não encontrado |

---

## Testando com curl

```bash
# Listar todos os produtos
curl -X GET http://localhost:8081/products

# Buscar produto por ID
curl -X GET http://localhost:8081/products/1

# Criar produto
curl -X POST http://localhost:8081/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Teclado", "description": "Teclado mecânico RGB", "value": 299.90, "quantity": 25}'

# Atualizar produto
curl -X PUT http://localhost:8081/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Teclado Pro", "description": "Teclado mecânico RGB sem fio", "value": 399.90, "quantity": 15}'

# Deletar produto
curl -X DELETE http://localhost:8081/products/1
```

---

## Documentação interativa (Swagger UI)

Com a aplicação rodando, acesse:

```text
http://localhost:8081/swagger-ui.html
```

Lá você encontra todos os endpoints documentados e pode testá-los direto pelo navegador, sem precisar de nenhuma ferramenta externa.

---

## Docker (detalhes)

O projeto tem um `Dockerfile` com build em múltiplos estágios: primeiro compila com Maven, depois copia apenas o `.jar` gerado para uma imagem menor com só o JRE, reduzindo bastante o tamanho final.

```text
Dockerfile         →  compila e empacota a aplicação
docker-compose.yml →  sobe o MySQL + a API juntos
```

Para rodar só a API manualmente (sem o Compose), passe as variáveis de ambiente do banco:

```bash
docker build -t products-api .

docker run -p 8081:8081 \
  -e DB_HOST=localhost \
  -e DB_PORT=3306 \
  -e DB_NAME=productsdb \
  -e DB_USER=root \
  -e DB_PASSWORD=sua_senha \
  products-api
```

---

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Data JPA** + **Hibernate**
- **Spring Validation** (Jakarta Bean Validation)
- **MySQL 8**
- **springdoc-openapi 3.0.2** (Swagger UI)
- **Lombok** (configurado — veja `README-LOMBOK.md`)
- **Docker** + **Docker Compose**
- **Maven**
