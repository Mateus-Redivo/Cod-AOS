package com.example.api.demo.service;

import com.example.api.demo.dto.ProdutoDTO;
import com.example.api.demo.model.Produto;
import com.example.api.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    
    // Constructor injection
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }
    
    /**
     * Converter Produto para ProdutoDTO
     */
    private ProdutoDTO convertToDTO(Produto produto) {
        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getQuantidade()
        );
    }
    
    /**
     * Converter ProdutoDTO para Produto
     */
    private Produto convertToEntity(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setId(produtoDTO.getId());
        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidade(produtoDTO.getQuantidade());
        return produto;
    }
    
    /**
     * Listar todos os produtos
     */
    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Buscar produto por ID
     */
    public Optional<ProdutoDTO> buscarPorId(Long id) {
        return produtoRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    /**
     * Criar novo produto
     */
    public ProdutoDTO criar(ProdutoDTO produtoDTO) {
        Produto produto = convertToEntity(produtoDTO);
        
        // Busca o menor ID disponível
        Long smallestAvailableId = produtoRepository.findSmallestAvailableId();
        if (smallestAvailableId != null) {
            produto.setId(smallestAvailableId);
        }
        
        Produto produtoSalvo = produtoRepository.save(produto);
        return convertToDTO(produtoSalvo);
    }
    
    /**
     * Atualizar produto existente
     */
    public Optional<ProdutoDTO> atualizar(Long id, ProdutoDTO produtoDTO) {
        if (!produtoRepository.existsById(id)) {
            return Optional.empty();
        }
        
        Produto produto = convertToEntity(produtoDTO);
        produto.setId(id);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return Optional.of(convertToDTO(produtoAtualizado));
    }
    
    /**
     * Excluir produto
     */
    public boolean excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            return false;
        }
        
        produtoRepository.deleteById(id);
        return true;
    }
}
