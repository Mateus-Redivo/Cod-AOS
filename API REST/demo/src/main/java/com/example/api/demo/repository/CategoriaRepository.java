package com.example.api.demo.repository;

import com.example.api.demo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Encontra o menor ID disponível
    @Query(value = "SELECT MIN(t1.id + 1) FROM categoria t1 LEFT JOIN categoria t2 ON t1.id + 1 = t2.id WHERE t2.id IS NULL", nativeQuery = true)
    Long findSmallestAvailableId();
}