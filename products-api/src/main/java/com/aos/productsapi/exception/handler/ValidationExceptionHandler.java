package com.aos.productsapi.exception.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aos.productsapi.exception.ApiError;

import jakarta.servlet.http.HttpServletRequest;

// ERRO 1 de 4: dados inválidos -> 400 Bad Request
//
// A requisição chegou certinha, o JSON foi entendido, mas o conteúdo não passou
// nas regras declaradas na entidade Product (@NotBlank, @Size, @Positive...).
//
// @Order(1) dá a este handler a maior prioridade. Isso importa porque o
// GlobalExceptionHandler trata Exception, que é a "mãe" de todas as exceções —
// sem a ordem definida, ele poderia pegar este erro antes e devolver 500.
@RestControllerAdvice
@Order(1)
public class ValidationExceptionHandler {

    // Dispara em POST e PUT quando o @Valid reprova algum campo do JSON,
    // por exemplo {"name": "", "value": -10}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // LinkedHashMap mantém os campos na ordem em que foram encontrados
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        // getFieldErrors() devolve um erro por campo reprovado
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Validation failed for the request body",
                request.getRequestURI(),
                fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
