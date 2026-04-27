package com.aos.productsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aos.productsapi.dto.ProductDTO;
import com.aos.productsapi.mapper.ProductMapper;
import com.aos.productsapi.repository.ProductRepository;

/**
 * Service layer responsible for all product business logic.
 * Delegates persistence to {@link ProductRepository} and uses
 * {@link ProductMapper} for entity/DTO conversion.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Injects the repository via constructor injection.
     *
     * @param productRepository the product repository
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return list of all products as DTOs
     */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param id the product ID
     * @return an Optional containing the DTO if found, or empty if not
     */
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDTO);
    }

    /**
     * Persists a new product.
     *
     * @param productDTO the data for the new product
     * @return the created product as a DTO (includes generated ID)
     */
    public ProductDTO createProduct(ProductDTO productDTO) {
        return ProductMapper.toDTO(productRepository.save(ProductMapper.toEntity(productDTO)));
    }

    /**
     * Updates an existing product's fields.
     *
     * @param id         the ID of the product to update
     * @param productDTO the new values to apply
     * @return an Optional with the updated DTO, or empty if the product was not found
     */
    public Optional<ProductDTO> updateProduct(Long id, ProductDTO productDTO) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setValue(productDTO.getValue());
            product.setQuantity(productDTO.getQuantity());
            return ProductMapper.toDTO(productRepository.save(product));
        });
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @return true if the product existed and was deleted, false otherwise
     */
    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) return false;
        productRepository.deleteById(id);
        return true;
    }
}
