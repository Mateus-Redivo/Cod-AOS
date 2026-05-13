package com.aos.productsapi.mapper;

import org.springframework.stereotype.Component;

import com.aos.productsapi.dto.ProductDTO;
import com.aos.productsapi.model.Product;

// @Component registra essa interface no contexto do Spring (necessário para injeção de dependência)
@Component
public interface ProductMapper {

    // Converte uma entidade Product em DTO para ser enviado como resposta da API
    static ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getValue(),
                product.getQuantity()
        );
    }

    // Converte um DTO recebido na requisição em entidade para ser salva no banco
    // O ID não é passado pois ainda não existe — o banco vai gerar na hora do save
    static Product toEntity(ProductDTO productDTO) {
        return new Product(
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getValue(),
                productDTO.getQuantity()
        );
    }
}
