package com.aos.productsapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing a product stored in the "product" table.
 */
@Entity
@Table(name = "product")
public class Product {

    /** Primary key, auto-incremented by the database. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Display name of the product. */
    @Column(nullable = false, length = 100)
    private String name;

    /** Short description of the product. */
    @Column(nullable = false, length = 255)
    private String description;

    /** Price of the product. */
    @Column(nullable = false)
    private double value;

    /** Number of units available in stock. */
    @Column(nullable = false)
    private int quantity;

    /** Required by JPA — not intended for direct use. */
    public Product() {
    }

    /**
     * Creates a new Product without an ID (used before persisting).
     *
     * @param name        the product name
     * @param description the product description
     * @param value       the product price
     * @param quantity    the available stock quantity
     */
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
