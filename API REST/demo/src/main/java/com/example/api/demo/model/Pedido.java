package com.example.api.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime dataPedido;
    private String status;
    
    @ManyToMany
    private List<Produto> produtos;
    
    private Double valorTotal;
    
    // Getters e Setters
}