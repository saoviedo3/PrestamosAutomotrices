package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {

    List<TipoDocumento> findByEstado(String estado);
    
    TipoDocumento findByNombre(String nombre);
    
    List<TipoDocumento> findByNombreContainingIgnoreCase(String nombre);
    
    boolean existsByNombre(String nombre);
} 