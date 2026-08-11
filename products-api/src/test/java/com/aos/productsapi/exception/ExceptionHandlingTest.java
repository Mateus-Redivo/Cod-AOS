package com.aos.productsapi.exception;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aos.productsapi.controller.ProductController;
import com.aos.productsapi.model.Product;
import com.aos.productsapi.repository.ProductRepository;

// Um teste para cada handler do pacote exception.handler.
// Serve como documentação executável: mostra qual requisição gera qual código
// HTTP e o que sai no corpo da resposta.
//
// @WebMvcTest sobe só a camada web (controller + os @RestControllerAdvice),
// sem banco. Por isso o repositório é substituído por um dublê com @MockitoBean.
@WebMvcTest(ProductController.class)
class ExceptionHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    @Test
    @DisplayName("Produto inexistente devolve 404 com corpo ApiError")
    void shouldReturnNotFoundForMissingProduct() throws Exception {
        given(productRepository.findById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Product not found with id 99"))
                .andExpect(jsonPath("$.path").value("/products/99"))
                .andExpect(jsonPath("$.timestamp").exists())
                // erro sem detalhe de campo não deve carregar a chave fieldErrors
                .andExpect(jsonPath("$.fieldErrors").doesNotExist());
    }

    @Test
    @DisplayName("Campos inválidos devolvem 400 com a lista de fieldErrors")
    void shouldReturnBadRequestWithFieldErrors() throws Exception {
        String invalidProduct = """
                {"name": "", "description": "Notebook", "value": -10, "quantity": -1}
                """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidProduct))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed for the request body"))
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.value").value("Value must be greater than zero"))
                .andExpect(jsonPath("$.fieldErrors.quantity").value("Quantity must be zero or greater"));
    }

    @Test
    @DisplayName("JSON malformado devolve 400 sem expor a mensagem do Jackson")
    void shouldReturnBadRequestForMalformedJson() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Notebook\",}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request body"));
    }

    @Test
    @DisplayName("Id não numérico na URL devolve 400, e não 500")
    void shouldReturnBadRequestForNonNumericId() throws Exception {
        mockMvc.perform(get("/products/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Parameter 'id' has an invalid value: abc"));
    }

    @Test
    @DisplayName("Verbo não suportado devolve 405")
    void shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(delete("/products"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405));
    }

    @Test
    @DisplayName("Content-Type não suportado devolve 415")
    void shouldReturnUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(MediaType.TEXT_PLAIN)
                .content("Notebook"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415));
    }

    @Test
    @DisplayName("Violação de restrição do banco devolve 409 sem vazar detalhe do MySQL")
    void shouldReturnConflictOnDataIntegrityViolation() throws Exception {
        given(productRepository.save(any(Product.class)))
                .willThrow(new DataIntegrityViolationException("Duplicate entry 'x' for key 'product.name_UNIQUE'"));

        String validProduct = """
                {"name": "Notebook", "description": "15 inch", "value": 999.99, "quantity": 5}
                """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validProduct))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("The operation conflicts with the data already stored"));
    }

    @Test
    @DisplayName("Falha inesperada devolve 500 com mensagem genérica")
    void shouldReturnInternalServerErrorOnUnexpectedFailure() throws Exception {
        given(productRepository.findById(1L)).willThrow(new IllegalStateException("connection pool exhausted"));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                // a mensagem original nunca pode aparecer na resposta HTTP
                .andExpect(jsonPath("$.message").value("An unexpected internal error occurred"));
    }
}
