package com.example.api.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private List<ProdutoDTO> produtos = new ArrayList<>(); // Alterado de List<Long> para List<ProdutoDTO>
    
    // Construtores
    public CategoriaDTO() {}
    
    public CategoriaDTO(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }
    
    // Getters e Setters existentes
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    // Alterar os métodos get/set de produtos
    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }
    
    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }
}