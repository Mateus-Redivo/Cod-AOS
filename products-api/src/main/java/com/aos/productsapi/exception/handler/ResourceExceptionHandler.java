package com.aos.productsapi.exception.handler;

import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aos.productsapi.exception.ApiError;
import com.aos.productsapi.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

// ERRO 3 de 4: problema com o produto no banco
//
// Aqui a requisição estava correta em forma e em conteúdo. O problema é o
// estado dos dados: o produto pedido não existe, ou o banco recusou a gravação.
@RestControllerAdvice
@Order(3)
public class ResourceExceptionHandler {

    // Produto não existe -> 404
    // Substitui o ResponseEntity.notFound().build() que ficava no controller.
    // A diferença: aquele 404 vinha com corpo vazio, este vem com o ApiError
    // dizendo o que não foi encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        // getMessage() devolve o texto que o controller passou ao lançar a exceção
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Banco recusou a gravação -> 409 Conflict
    // Acontece quando o INSERT ou UPDATE bate em uma regra do MySQL: texto maior
    // que o length da coluna, chave duplicada, campo obrigatório nulo.
    // O 409 diz "seu pedido conflita com os dados que já existem"; um 500 daria
    // a entender que o servidor quebrou
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(HttpServletRequest request) {
        // A mensagem do MySQL expõe nomes de tabelas e colunas, então não sai na resposta
        ApiError error = new ApiError(
                HttpStatus.CONFLICT,
                "The operation conflicts with the data already stored",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
