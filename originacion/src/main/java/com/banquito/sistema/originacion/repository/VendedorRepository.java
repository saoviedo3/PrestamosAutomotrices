package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    
    boolean existsByEmail(String email);
} 