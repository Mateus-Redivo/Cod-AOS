package com.aos.productsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aos.productsapi.model.Product;

/**
 * Spring Data JPA repository for {@link Product} entities.
 * Provides standard CRUD operations out of the box via {@link JpaRepository}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
