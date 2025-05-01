package com.example.api.demo.repository;

import com.example.api.demo.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Encontra o menor ID disponível
    @Query(value = "SELECT MIN(t1.id + 1) FROM produto t1 LEFT JOIN produto t2 ON t1.id + 1 = t2.id WHERE t2.id IS NULL", nativeQuery = true)
    Long findSmallestAvailableId();
}
