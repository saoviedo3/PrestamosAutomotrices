package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.banquito.sistema.originacion.model.ClienteProspecto;

@Repository
public interface ClienteProspectoRepository extends JpaRepository<ClienteProspecto, Long> {

    Optional<ClienteProspecto> findByCedula(String cedula);
    
    List<ClienteProspecto> findByEstado(String estado);
    
    List<ClienteProspecto> findByNombreAndApellido(String nombre, String apellido);
    
    Optional<ClienteProspecto> findByEmail(String email);
    
    List<ClienteProspecto> findByEstadoOrderByNombreAsc(String estado);
    
    boolean existsByCedula(String cedula);
    
    boolean existsByEmail(String email);
    
    List<ClienteProspecto> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
} 