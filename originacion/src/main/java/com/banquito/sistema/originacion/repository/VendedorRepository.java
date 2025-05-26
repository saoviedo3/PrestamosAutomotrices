package com.banquito.sistema.originacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    List<Vendedor> findByEstado(String estado);
    
    List<Vendedor> findByIdConcesionario(Long idConcesionario);
    
    List<Vendedor> findByNombreContainingIgnoreCase(String nombre);
    
    boolean existsByEmail(String email);
} 