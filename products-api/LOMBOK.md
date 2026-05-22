# Lombok — O que é e como usar neste projeto

## O que é o Lombok

Lombok é uma biblioteca Java que elimina código repetitivo. Em Java, toda classe que guarda dados precisa de getters, setters, construtores e às vezes `toString()` e `equals()`. Esses métodos não têm lógica — são sempre iguais — mas você precisa escrevê-los ou gerá-los manualmente.

O Lombok faz isso por você através de anotações. Em vez de escrever 60 linhas de getters e setters, você escreve uma anotação e o Lombok gera o código na hora da compilação.

---

## Por que ele está no projeto mas não está sendo usado

O projeto foi escrito de forma manual e explícita de propósito: para que você veja e entenda cada getter, setter e construtor que existe. Antes de usar uma ferramenta que gera código automaticamente, é importante saber o que ela está gerando.

Após entender o que cada método faz, o Lombok passa a ser um ganho real de produtividade — não um atalho para pular o aprendizado.

---

## Principais anotações

### `@Getter` e `@Setter`

Geram os métodos `get` e `set` para todos os campos da classe.

```java
// Sem Lombok — você escreve isso tudo:
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }
// ... repete para cada campo

// Com Lombok — só isso:
@Getter
@Setter
public class Product { ... }
```

---

### `@NoArgsConstructor`

Gera o construtor vazio (sem parâmetros).

```java
// Sem Lombok:
public Product() {}

// Com Lombok:
@NoArgsConstructor
public class Product { ... }
```

---

### `@AllArgsConstructor`

Gera um construtor com todos os campos como parâmetros.

```java
// Sem Lombok:
public Product(String name, String description, double value, int quantity) {
    this.name = name;
    this.description = description;
    this.value = value;
    this.quantity = quantity;
}

// Com Lombok:
@AllArgsConstructor
public class Product { ... }
```

---

### `@RequiredArgsConstructor`

Gera um construtor apenas com os campos `final` e os marcados com `@NonNull`. É a anotação usada para injeção de dependência no Spring — substitui os construtores escritos manualmente nas classes `@Service` e `@Controller`.

```java
// Sem Lombok — no ProductService:
public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
}

// Com Lombok — o construtor some, o Spring ainda injeta normalmente:
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    // Lombok gera o construtor automaticamente
}
```

---

### `@ToString`

Gera o método `toString()` com todos os campos.

```java
// Sem Lombok:
@Override
public String toString() {
    return "Product{id=" + id + ", name='" + name + "', ...}";
}

// Com Lombok:
@ToString
public class Product { ... }
```

---

### `@Data`

É um atalho que combina cinco anotações de uma vez: `@Getter`, `@Setter`, `@NoArgsConstructor` implícito via `@RequiredArgsConstructor`, `@ToString` e `@EqualsAndHashCode`.

```java
// Com @Data, toda essa classe:
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product { ... }

// Vira isso:
@Data
@AllArgsConstructor
public class Product { ... }
```

> `@Data` não inclui `@AllArgsConstructor` — se quiser o construtor completo junto, precisa adicionar separado.

---

## Como ficaria o projeto usando Lombok

### `model/Product.java`

```java
package com.aos.productsapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    // Construtor sem ID (o banco gera) — ainda precisa ser escrito manualmente,
    // pois @AllArgsConstructor gera um com todos os campos incluindo o id
    public Product(String name, String description, double value, int quantity) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.quantity = quantity;
    }
}
```

De 87 linhas para ~30. O conteúdo é o mesmo — o Lombok só elimina a parte mecânica.

---

### `dto/ProductDTO.java`

```java
package com.aos.productsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "Product data transfer object")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Name of the product", example = "Notebook")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Schema(description = "Short description of the product", example = "15-inch laptop")
    private String description;

    @Positive(message = "Value must be greater than zero")
    @Schema(description = "Price of the product", example = "999.99")
    private double value;

    @PositiveOrZero(message = "Quantity must be zero or greater")
    @Schema(description = "Available stock quantity", example = "50")
    private int quantity;
}
```

---

### `service/ProductService.java` e `controller/ProductController.java`

Nesses dois arquivos, o Lombok elimina o construtor de injeção de dependência:

```java
// Antes — construtor escrito manualmente:
public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
}

// Depois — anotação no topo da classe, construtor some:
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    // sem construtor — o Lombok gera nos bastidores
}
```

---

## Como ativar

O Lombok já está configurado no `pom.xml` deste projeto como annotation processor. Você não precisa instalar nada extra no Maven.

Se estiver usando o **IntelliJ IDEA**, instale o plugin do Lombok:

```text
File → Settings → Plugins → buscar "Lombok" → Install → Reiniciar
```

Se estiver usando o **VS Code**, instale a extensão:

```text
Extensions → buscar "Lombok Annotations Support for VS Code" → Install
```

Sem o plugin instalado na IDE, o código vai compilar e rodar normalmente, mas a IDE vai sublinhar os campos com erros de "método não encontrado" — porque ela não vê os métodos que o Lombok gera.

---

## Resumo

| Anotação | O que gera |
| --- | --- |
| `@Getter` | Todos os `get...()` |
| `@Setter` | Todos os `set...()` |
| `@NoArgsConstructor` | Construtor vazio `Classe() {}` |
| `@AllArgsConstructor` | Construtor com todos os campos |
| `@RequiredArgsConstructor` | Construtor com os campos `final` |
| `@ToString` | Método `toString()` |
| `@EqualsAndHashCode` | Métodos `equals()` e `hashCode()` |
| `@Data` | `@Getter` + `@Setter` + `@RequiredArgsConstructor` + `@ToString` + `@EqualsAndHashCode` |
