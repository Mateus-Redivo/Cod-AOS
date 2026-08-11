package com.aos.productsapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aos.productsapi.exception.ResourceNotFoundException;
import com.aos.productsapi.model.Product;
import com.aos.productsapi.repository.ProductRepository;

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

    // Versão MVC simplificada: o controller conversa direto com o repositório.
    // Não existem mais as camadas de Service (que só repassava chamadas),
    // DTO e Mapper (que só copiavam os mesmos campos de um objeto para o outro).
    private final ProductRepository productRepository;

    // Injeção de dependência via construtor
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // @Operation descreve o endpoint na documentação do Swagger
    @Operation(summary = "List all products")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    // @GetMapping mapeia requisições GET para /products
    @GetMapping
    public List<Product> getAll() {
        // findAll() já devolve a lista pronta — sem stream e sem conversão para DTO
        return productRepository.findAll();
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    // @GetMapping("/{id}") mapeia GET /products/{id} — o {id} é um parâmetro dinâmico na URL
    @GetMapping("/{id}")
    // @PathVariable extrai o valor de {id} da URL e injeta no parâmetro do método
    public Product getById(@PathVariable Long id) {
        // O controller não monta mais o 404 na mão: ele descreve só o caminho
        // feliz e lança a exceção quando não há produto. Quem transforma isso em
        // resposta HTTP é o ResourceExceptionHandler, no pacote exception.handler
        return findProductOrThrow(id);
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    // @PostMapping mapeia requisições POST para /products
    @PostMapping
    // @Valid ativa as validações declaradas na entidade Product. Se alguma falhar,
    // o método nem é executado: o Spring lança MethodArgumentNotValidException,
    // que o ValidationExceptionHandler transforma em 400.
    // @RequestBody desserializa o JSON da requisição direto para um objeto Product
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        // O id chega sempre nulo (o Jackson o ignora na entrada), então o save() faz INSERT
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @Operation(summary = "Update an existing product")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product product) {
        // Confirma que o produto existe antes de atualizar; se não existir,
        // findProductOrThrow interrompe a requisição com o 404
        findProductOrThrow(id);

        // Com o id preenchido, o save() faz UPDATE em vez de INSERT
        product.setId(id);
        return productRepository.save(product);
    }

    @Operation(summary = "Delete a product")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product); // remove o registro do banco
        return ResponseEntity.noContent().build(); // retorna 204 No Content
    }

    // Busca o produto ou lança a exceção de "não encontrado".
    // Deixar isso em um método só evita repetir a mesma verificação nos três
    // endpoints e garante que todos devolvam exatamente a mesma mensagem
    private Product findProductOrThrow(Long id) {
        // orElseThrow recebe uma função que só roda se o Optional vier vazio
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }
}
