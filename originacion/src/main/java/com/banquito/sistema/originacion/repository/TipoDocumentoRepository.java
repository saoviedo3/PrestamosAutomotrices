package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.TipoDocumento;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
    
    Optional<TipoDocumento> findByNombre(String nombre);
    
    List<TipoDocumento> findByEstado(String estado);
    
    boolean existsByNombre(String nombre);
} 