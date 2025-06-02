package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.ClienteProspecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteProspectoRepository extends JpaRepository<ClienteProspecto, Long> {
    Optional<ClienteProspecto> findByCedula(String cedula);
    List<ClienteProspecto> findByEstado(String estado);
    List<ClienteProspecto> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
    boolean existsByCedula(String cedula);
} 