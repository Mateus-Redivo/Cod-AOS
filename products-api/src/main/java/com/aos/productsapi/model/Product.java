package com.aos.productsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// @Entity diz ao JPA que essa classe representa uma tabela no banco de dados
@Entity
// @Table define o nome da tabela que será criada/usada no banco
@Table(name = "product")
// Sem DTO, essa mesma classe é o contrato da API: o JSON que entra e sai dos
// endpoints é montado a partir dela. Por isso ela acumula três papéis de
// anotações: JPA (banco), Bean Validation (@Valid) e Swagger (documentação).
@Schema(description = "Product")
public class Product {

    // @Id marca esse campo como a chave primária da tabela
    @Id
    // @GeneratedValue com IDENTITY faz o banco gerar o ID automaticamente (auto increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @JsonProperty(READ_ONLY) faz o Jackson ignorar o id que vier no corpo da
    // requisição, mas continuar devolvendo ele na resposta. Sem isso, um POST
    // com "id": 5 sobrescreveria o produto 5 em vez de criar um novo — essa era
    // a proteção que o ProductMapper dava de graça ao não copiar o id do DTO.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the product", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    // Long (objeto) em vez de long (primitivo) porque só o objeto aceita null.
    // Antes de salvar, o produto ainda não tem id: com Long ele fica null e o JPA
    // entende "entidade nova, gere o id"; com long ele viria 0, que o JPA poderia
    // interpretar como um id já existente. O null também é o que permite checar
    // "esse produto já foi persistido?" nos getters/serviços.
    private Long id;

    // @Column configura a coluna no banco: nullable = false torna o campo obrigatório
    // @NotBlank impede que o campo venha vazio ou só com espaços na requisição
    // @Size define os limites mínimo e máximo de caracteres aceitos
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Name of the product", example = "Notebook")
    private String name;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Schema(description = "Short description of the product", example = "15-inch laptop")
    private String description;

    // @Positive garante que o valor enviado seja maior que zero
    @Column(nullable = false)
    @Positive(message = "Value must be greater than zero")
    @Schema(description = "Price of the product", example = "999.99")
    private double value;

    // @PositiveOrZero aceita zero ou qualquer número positivo (estoque pode ser zerado)
    @Column(nullable = false)
    @PositiveOrZero(message = "Quantity must be zero or greater")
    @Schema(description = "Available stock quantity", example = "50")
    private int quantity;

    // Construtor vazio obrigatório pelo JPA para instanciar a entidade internamente.
    // Agora ele também é usado pelo Jackson para desserializar o JSON da requisição.
    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', description='" + description + "', value=" + value
                + ", quantity=" + quantity + "}";
    }
}
