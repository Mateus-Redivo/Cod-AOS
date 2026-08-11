package com.aos.productsapi.exception;

// Exceção lançada quando o produto pedido não existe no banco.
//
// Antes, o controller resolvia isso com ResponseEntity.notFound().build():
// um 404 correto, mas de corpo vazio. Com uma exceção própria, o controller
// escreve só o caminho feliz e o ResourceExceptionHandler monta o 404 com o
// mesmo corpo ApiError de todos os outros erros.
//
// Estende RuntimeException para não precisar ser declarada com "throws" nem
// capturada em cada camada até chegar ao handler.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        // super(...) guarda a mensagem que o handler vai ler com getMessage()
        super(message);
    }
}
