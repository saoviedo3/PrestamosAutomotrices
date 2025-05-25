package com.banquito.sistema.originacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    Optional<Vendedor> findByCodigo(String codigo);
    
    Optional<Vendedor> findByCedula(String cedula);
    
    List<Vendedor> findByEstado(String estado);
    
    List<Vendedor> findByConcesionario_Id(Long concesionarioId);
    
    List<Vendedor> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);
    
    boolean existsByCodigo(String codigo);
    
    boolean existsByCedula(String cedula);
    
    boolean existsByEmail(String email);
} 