package com.aos.productsapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aos.productsapi.dto.ProductDTO;
import com.aos.productsapi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// @RestController combina @Controller + @ResponseBody: transforma automaticamente o retorno dos métodos em JSON
@RestController
// @RequestMapping define o prefixo de rota para todos os endpoints deste controller
@RequestMapping("/products")
// @Tag agrupa os endpoints sob o nome "Products" na documentação do Swagger
@Tag(name = "Products", description = "CRUD operations for products")
public class ProductController {

    private final ProductService productService;

    // Injeção de dependência via construtor
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // @Operation descreve o endpoint na documentação do Swagger
    @Operation(summary = "List all products")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    // @GetMapping mapeia requisições GET para /products
    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    // @GetMapping("/{id}") mapeia GET /products/{id} — o {id} é um parâmetro dinâmico na URL
    @GetMapping("/{id}")
    // @PathVariable extrai o valor de {id} da URL e injeta no parâmetro do método
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)             // se encontrou: retorna 200 com o produto no corpo
                .orElse(ResponseEntity.notFound().build()); // se não encontrou: retorna 404 sem corpo
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    // @PostMapping mapeia requisições POST para /products
    @PostMapping
    // @Valid ativa as validações definidas no ProductDTO antes de executar o método
    // @RequestBody desserializa o JSON da requisição para um objeto ProductDTO
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO) {
        // ResponseEntity.status(201) retorna o HTTP 201 Created com o produto criado no corpo
        return ResponseEntity.status(201).body(productService.createProduct(productDTO));
    }

    @Operation(summary = "Update an existing product")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a product")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productService.deleteProduct(id)
                ? ResponseEntity.noContent().build() // retorna 204 No Content se deletou com sucesso
                : ResponseEntity.notFound().build(); // retorna 404 se o produto não existia
    }
}
