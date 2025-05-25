package com.banquito.sistema.originacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.Concesionario;

@Repository
public interface ConcesionarioRepository extends JpaRepository<Concesionario, Long> {

    Optional<Concesionario> findByCodigo(String codigo);
    
    List<Concesionario> findByEstado(String estado);
    
    List<Concesionario> findByCiudad(String ciudad);
    
    List<Concesionario> findByProvincia(String provincia);
    
    List<Concesionario> findByNombreContainingIgnoreCase(String nombre);
    
    boolean existsByCodigo(String codigo);
    
    boolean existsByEmail(String email);
} 