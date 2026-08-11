package com.aos.productsapi.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aos.productsapi.exception.ApiError;

import jakarta.servlet.http.HttpServletRequest;

// ERRO 4 de 4: rede de segurança -> 500 Internal Server Error
//
// Pega qualquer exceção que os três handlers anteriores não trataram:
// NullPointerException, banco fora do ar, bug de lógica. É o que garante que a
// API nunca devolva a página de erro padrão do servidor, que mostra a stack
// trace inteira para quem chamou.
//
// @Order(LOWEST_PRECEDENCE) coloca este handler por último, porque ele trata
// Exception — a superclasse de todas. Sem isso ele poderia interceptar um erro
// de validação antes do handler certo e virar um 500 genérico.
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    // O Logger escreve no console/arquivo de log do servidor
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
        // Passar a exceção inteira para o log guarda a stack trace no servidor —
        // sem ela, o problema fica impossível de investigar depois
        log.error("Unexpected error on {} {}", request.getMethod(), request.getRequestURI(), ex);

        // Já a resposta HTTP leva um texto fixo, sem o ex.getMessage(): essa
        // mensagem costuma conter caminhos de arquivo, SQL e nomes de classes
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal error occurred",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
