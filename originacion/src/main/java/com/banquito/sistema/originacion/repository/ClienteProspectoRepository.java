package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.ClienteProspecto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteProspectoRepository extends JpaRepository<ClienteProspecto, Long> {
    
    Optional<ClienteProspecto> findByCedula(String cedula);
    
    List<ClienteProspecto> findByEstado(String estado);
    
    List<ClienteProspecto> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
    
    boolean existsByCedula(String cedula);
    
    boolean existsByEmail(String email);
} 