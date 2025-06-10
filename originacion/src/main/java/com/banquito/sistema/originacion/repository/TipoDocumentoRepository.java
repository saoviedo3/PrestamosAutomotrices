package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.banquito.sistema.originacion.model.TipoDocumento;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {

    List<TipoDocumento> findByEstado(String estado);
    
    Optional<TipoDocumento> findByNombre(String nombre);
    
    List<TipoDocumento> findByEstadoOrderByNombre(String estado);
    
    boolean existsByNombre(String nombre);
} 