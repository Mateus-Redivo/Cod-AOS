package com.aos.productsapi.exception.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.aos.productsapi.exception.ApiError;

import jakarta.servlet.http.HttpServletRequest;

// ERRO 2 de 4: requisição malformada -> a chamada nem chegou ao controller
//
// São falhas que o Spring encontra ao traduzir a requisição HTTP em uma chamada
// de método: JSON quebrado, id que não é número, rota que não existe, verbo
// errado, Content-Type errado.
//
// Repare que cada caso tem um código HTTP diferente. Devolver 400 para tudo
// funcionaria, mas esconderia do cliente a diferença entre "seu JSON está
// errado" e "esta rota não aceita POST".
@RestControllerAdvice
@Order(2)
public class RequestExceptionHandler {

    // JSON quebrado ou corpo vazio -> 400
    // Acontece antes da validação: o Jackson nem conseguiu montar o objeto
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(HttpServletRequest request) {
        // A mensagem original do Jackson traz nomes de classes Java e detalhes
        // internos, então ela nunca é repassada: usamos um texto nosso
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON request body",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Parâmetro da URL com tipo errado -> 400
    // Exemplo: GET /products/abc, em que "abc" não vira o Long id esperado.
    // Sem este handler o Spring devolveria 500, dando a entender que o servidor
    // falhou — quando na verdade o pedido é que estava errado
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String message = "Parameter '%s' has an invalid value: %s".formatted(ex.getName(), ex.getValue());

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Rota que não existe -> 404
    // Exemplo: GET /product (no singular)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleUnknownRoute(HttpServletRequest request) {
        String message = "No endpoint found for %s %s".formatted(request.getMethod(), request.getRequestURI());

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, message, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Verbo HTTP que a rota não aceita -> 405
    // Exemplo: DELETE /products (sem informar o id)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        String message = "Method %s is not supported for this endpoint".formatted(ex.getMethod());

        ApiError error = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, message, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    // Formato de corpo que a API não sabe ler -> 415
    // Acontece quando o cliente esquece o Content-Type: application/json.
    // É diferente do 400: o conteúdo pode até estar certo, mas veio em um
    // formato que a API não aceita
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleUnsupportedMediaType(HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Content type not supported. Use application/json",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }
}
