package com.example.api.demo.service;

import org.springframework.stereotype.Service;

import com.example.api.demo.model.Produto;
import com.example.api.demo.dto.CategoriaDTO;
import com.example.api.demo.dto.ProdutoDTO;
import com.example.api.demo.repository.CategoriaRepository;
import com.example.api.demo.repository.PedidoRepository;
import com.example.api.demo.repository.ProdutoRepository;

@Service
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    private ProdutoDTO convertProdutoToDTO(Produto produto) {
        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getQuantidade()
        );
    }

    private Produto convertDTOToProduto(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setId(produtoDTO.getId());
        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidade(produtoDTO.getQuantidade());
        return produto;
    }
}
