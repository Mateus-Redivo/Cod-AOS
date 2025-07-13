package com.example.api.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.api.demo.dto.CategoriaDTO;
import com.example.api.demo.dto.ProdutoDTO;
import com.example.api.demo.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    
    private final CategoriaService categoriaService;
    
    // Constructor injection
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Listar todas as categorias
    @GetMapping
    public List<CategoriaDTO> listarTodas() {
        return categoriaService.listarTodas();
    }
    
    // Buscar categoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obterPorId(@PathVariable Long id) {
        Optional<CategoriaDTO> categoria = categoriaService.buscarPorId(id);
        
        return categoria
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Criar nova categoria
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDTO adicionar(@RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.criar(categoriaDTO);
    }
    
    // Atualizar categoria existente
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        Optional<CategoriaDTO> categoriaAtualizada = categoriaService.atualizar(id, categoriaDTO);
        
        return categoriaAtualizada
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Remover categoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        boolean excluido = categoriaService.excluir(id);
        
        if (excluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar produtos de uma categoria
    @GetMapping("/{id}/produtos")
    public ResponseEntity<List<ProdutoDTO>> listarProdutosDaCategoria(@PathVariable Long id) {
        Optional<List<ProdutoDTO>> produtos = categoriaService.listarProdutosDaCategoria(id);
        
        return produtos
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Adicionar produto à categoria
    @PostMapping("/{categoriaId}/produtos/{produtoId}")
    public ResponseEntity<Void> adicionarProduto(@PathVariable Long categoriaId, @PathVariable Long produtoId) {
        boolean adicionado = categoriaService.adicionarProduto(categoriaId, produtoId);
        
        if (adicionado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Remover produto da categoria
    @DeleteMapping("/{categoriaId}/produtos/{produtoId}")
    public ResponseEntity<Void> removerProduto(@PathVariable Long categoriaId, @PathVariable Long produtoId) {
        boolean removido = categoriaService.removerProduto(categoriaId, produtoId);
        
        if (removido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}