package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.Concesionario;

@Repository
public interface ConcesionarioRepository extends JpaRepository<Concesionario, Long> {
    
    boolean existsByEmailContacto(String emailContacto);
} 