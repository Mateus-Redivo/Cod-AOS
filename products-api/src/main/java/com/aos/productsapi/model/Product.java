package com.aos.productsapi.model;

import jakarta.persistence.*;

// @Entity diz ao JPA que essa classe representa uma tabela no banco de dados
@Entity
// @Table define o nome da tabela que será criada/usada no banco
@Table(name = "product")
public class Product {

    // @Id marca esse campo como a chave primária da tabela
    @Id
    // @GeneratedValue com IDENTITY faz o banco gerar o ID automaticamente (auto increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column configura a coluna: nullable = false significa que o campo é obrigatório no banco
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private int quantity;

    // Construtor vazio obrigatório pelo JPA para instanciar a entidade internamente
    public Product() {
    }

    // Construtor usado ao criar um produto novo (sem ID, pois o banco gera)
    public Product(String name, String description, double value, int quantity) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.quantity = quantity;
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
