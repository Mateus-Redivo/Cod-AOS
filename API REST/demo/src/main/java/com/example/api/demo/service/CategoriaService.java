package com.example.api.demo.service;

import com.example.api.demo.dto.CategoriaDTO;
import com.example.api.demo.dto.ProdutoDTO;
import com.example.api.demo.model.Categoria;
import com.example.api.demo.model.Produto;
import com.example.api.demo.repository.CategoriaRepository;
import com.example.api.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    
    // Constructor injection
    public CategoriaService(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }
    
    /**
     * Converter Produto para ProdutoDTO
     */
    private ProdutoDTO convertProdutoToDTO(Produto produto) {
        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getQuantidade()
        );
    }
    
    /**
     * Converter Categoria para CategoriaDTO
     */
    private CategoriaDTO convertToDTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO(
            categoria.getId(),
            categoria.getNome(),
            categoria.getDescricao()
        );
        
        List<ProdutoDTO> produtosDTO = categoria.getProdutos()
            .stream()
            .map(this::convertProdutoToDTO)
            .collect(Collectors.toList());
        
        dto.setProdutos(produtosDTO);
        return dto;
    }
    
    /**
     * Listar todas as categorias
     */
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Buscar categoria por ID
     */
    public Optional<CategoriaDTO> buscarPorId(Long id) {
        return categoriaRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    /**
     * Criar nova categoria
     */
    @Transactional
    public CategoriaDTO criar(CategoriaDTO categoriaDTO) {
        if (categoriaDTO.getNome() == null || categoriaDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser vazio");
        }
        
        Categoria categoria = convertToEntity(categoriaDTO);
        
        // Busca o menor ID disponível
        Long smallestAvailableId = categoriaRepository.findSmallestAvailableId();
        if (smallestAvailableId != null) {
            categoria.setId(smallestAvailableId);
        }
        
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return convertToDTO(categoriaSalva);
    }
    
    /**
     * Atualizar categoria existente
     */
    @Transactional
    public Optional<CategoriaDTO> atualizar(Long id, CategoriaDTO categoriaDTO) {
        if (categoriaDTO.getNome() == null || categoriaDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser vazio");
        }
        
        if (!categoriaRepository.existsById(id)) {
            return Optional.empty();
        }
        
        Categoria categoria = convertToEntity(categoriaDTO);
        categoria.setId(id);
        Categoria categoriaAtualizada = categoriaRepository.save(categoria);
        return Optional.of(convertToDTO(categoriaAtualizada));
    }
    
    /**
     * Excluir categoria
     */
    @Transactional
    public boolean excluir(Long id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            
            // Remove as referências dos produtos para esta categoria
            categoria.getProdutos().forEach(produto -> produto.setCategoria(null));
            
            categoriaRepository.delete(categoria);
            return true;
        }
        
        return false;
    }
    
    /**
     * Listar produtos de uma categoria
     */
    public Optional<List<ProdutoDTO>> listarProdutosDaCategoria(Long id) {
        return categoriaRepository.findById(id)
            .map(categoria -> categoria.getProdutos()
                .stream()
                .map(this::convertProdutoToDTO)
                .collect(Collectors.toList()));
    }
    
    /**
     * Adicionar produto à categoria
     */
    @Transactional
    public boolean adicionarProduto(Long categoriaId, Long produtoId) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);
        Optional<Produto> produtoOpt = produtoRepository.findById(produtoId);
        
        if (categoriaOpt.isPresent() && produtoOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            Produto produto = produtoOpt.get();
            
            // Verifica se o produto já está na categoria
            if (categoria.getProdutos().contains(produto)) {
                return false;
            }
            
            categoria.addProduto(produto);
            produto.setCategoria(categoria); // Importante para manter a consistência bidirecional
            categoriaRepository.save(categoria);
            return true;
        }
        
        return false;
    }
    
    /**
     * Remover produto da categoria
     */
    @Transactional
    public boolean removerProduto(Long categoriaId, Long produtoId) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);
        Optional<Produto> produtoOpt = produtoRepository.findById(produtoId);
        
        if (categoriaOpt.isPresent() && produtoOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            Produto produto = produtoOpt.get();
            
            if (!categoria.getProdutos().contains(produto)) {
                return false;
            }
            
            categoria.removeProduto(produto);
            produto.setCategoria(null); // Remove a referência do produto para a categoria
            categoriaRepository.save(categoria);
            return true;
        }
        
        return false;
    }
    
    /**
     * Converter CategoriaDTO para Categoria
     */
    private Categoria convertToEntity(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        return categoria;
    }
}