package com.aos.productsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aos.productsapi.dto.ProductDTO;
import com.aos.productsapi.mapper.ProductMapper;
import com.aos.productsapi.repository.ProductRepository;

// @Service registra essa classe como componente de serviço no Spring
// É aqui que fica a lógica de negócio da aplicação
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Injeção de dependência via construtor: o Spring injeta o repositório automaticamente
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()  // busca todos os produtos do banco como lista de entidades
                .stream()                   // transforma a lista em um stream para processar elemento por elemento
                .map(ProductMapper::toDTO)  // converte cada entidade Product em um ProductDTO
                .toList();                  // coleta os resultados de volta em uma lista
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)  // busca o produto pelo ID; retorna Optional (pode ser vazio)
                .map(ProductMapper::toDTO);    // se encontrou, converte para DTO; se não, mantém o Optional vazio
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        // toEntity converte o DTO em entidade → save persiste no banco → toDTO converte o resultado de volta
        return ProductMapper.toDTO(productRepository.save(ProductMapper.toEntity(productDTO)));
    }

    public Optional<ProductDTO> updateProduct(Long id, ProductDTO productDTO) {
        return productRepository.findById(id)  // tenta encontrar o produto pelo ID
                .map(product -> {              // se existir, executa o bloco com o produto encontrado
                    product.setName(productDTO.getName());
                    product.setDescription(productDTO.getDescription());
                    product.setValue(productDTO.getValue());
                    product.setQuantity(productDTO.getQuantity());
                    return ProductMapper.toDTO(productRepository.save(product)); // salva as alterações e retorna o DTO
                });
        // se o produto não existir, o Optional chega vazio aqui e o .map não executa — retorna Optional.empty()
    }

    public boolean deleteProduct(Long id) {
        // existsById verifica se existe um produto com esse ID antes de tentar deletar
        if (!productRepository.existsById(id)) return false;
        productRepository.deleteById(id); // remove o registro do banco
        return true;
    }
}
