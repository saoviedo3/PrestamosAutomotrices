package com.banquito.sistema.originacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.Concesionario;

@Repository
public interface ConcesionarioRepository extends JpaRepository<Concesionario, Long> {

    List<Concesionario> findByEstado(String estado);
    
    List<Concesionario> findByRazonSocialContainingIgnoreCase(String razonSocial);
    
    boolean existsByEmailContacto(String emailContacto);
} 