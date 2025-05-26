package com.banquito.sistema.originacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sistema.originacion.model.DocumentoAdjunto;

import java.util.List;

@Repository
public interface DocumentoAdjuntoRepository extends JpaRepository<DocumentoAdjunto, Long> {
    
    List<DocumentoAdjunto> findByIdSolicitud(Long idSolicitud);
    
    List<DocumentoAdjunto> findByIdTipoDocumento(Long idTipoDocumento);
    
    List<DocumentoAdjunto> findByIdSolicitudAndIdTipoDocumento(Long idSolicitud, Long idTipoDocumento);
    
    boolean existsByIdSolicitudAndIdTipoDocumento(Long idSolicitud, Long idTipoDocumento);
} 