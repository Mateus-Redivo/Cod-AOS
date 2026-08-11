package com.aos.productsapi.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

// Corpo padrão de erro da API: todo erro, de qualquer tipo, sai neste formato.
// Ter um formato único é o que permite ao frontend escrever UM tratamento de
// erro, em vez de um para cada endpoint.
//
// É um record (Java 16+): uma classe imutável em que o compilador já gera o
// construtor e os getters. Como o objeto só é criado e virado JSON, não precisa
// de setters.
//
// NON_NULL faz o Jackson omitir os campos nulos do JSON — assim o "fieldErrors"
// só aparece na resposta quando existe algo para mostrar.
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(

        // Quando o erro aconteceu
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        // Código HTTP numérico: 400, 404, 500...
        int status,

        // Nome do código HTTP: "Bad Request", "Not Found"...
        String error,

        // Explicação do problema em linguagem de gente
        String message,

        // Rota que falhou, ex.: "/products/99"
        String path,

        // Campo -> mensagem. Só é preenchido em erros de validação
        Map<String, String> fieldErrors) {

    // Construtor para erros simples, sem detalhe de campo.
    // O this(...) chama o construtor completo que o record gerou.
    public ApiError(HttpStatus status, String message, String path) {
        this(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path, null);
    }

    // Construtor para erros de validação, que levam a lista de campos inválidos
    public ApiError(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {
        this(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path, fieldErrors);
    }
}
