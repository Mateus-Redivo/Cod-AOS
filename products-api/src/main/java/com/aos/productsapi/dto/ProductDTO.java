package com.aos.productsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Product data transfer object used for API requests and responses.
 */
@Schema(description = "Product data transfer object")
public class ProductDTO {

    /** Unique identifier of the product. */
    @Schema(description = "Unique identifier of the product", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    /** Name of the product. */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Name of the product", example = "Notebook")
    private String name;

    /** Short description of the product. */
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Schema(description = "Short description of the product", example = "15-inch laptop")
    private String description;

    /** Price of the product. */
    @Positive(message = "Value must be greater than zero")
    @Schema(description = "Price of the product", example = "999.99")
    private double value;

    /** Available stock quantity. */
    @PositiveOrZero(message = "Quantity must be zero or greater")
    @Schema(description = "Available stock quantity", example = "50")
    private int quantity;

    public ProductDTO(Long id, String name, String description, double value, int quantity) {
        this.id = id;
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
}
