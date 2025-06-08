package com.banquito.sistema.contratos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.banquito.sistema.contratos.model.TipoDocumento;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {

    List<TipoDocumento> findByEstado(String estado);
    
    Optional<TipoDocumento> findByNombre(String nombre);
    
    List<TipoDocumento> findByEstadoOrderByNombre(String estado);
    
    boolean existsByNombre(String nombre);
} 