package com.banquito.sistema.contratos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

import com.banquito.sistema.contratos.model.DocumentoAdjunto;

public interface DocumentoAdjuntoRepository extends JpaRepository<DocumentoAdjunto, Long> {

    List<DocumentoAdjunto> findByIdSolicitud(Long idSolicitud);
    
    List<DocumentoAdjunto> findByIdTipoDocumento(Long idTipoDocumento);
    
    List<DocumentoAdjunto> findByIdSolicitudAndIdTipoDocumento(Long idSolicitud, Long idTipoDocumento);
    
    List<DocumentoAdjunto> findByFechaCargadoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<DocumentoAdjunto> findByIdSolicitudOrderByFechaCargadoDesc(Long idSolicitud);
    
    boolean existsByIdSolicitudAndIdTipoDocumento(Long idSolicitud, Long idTipoDocumento);
    
    Long countByIdSolicitud(Long idSolicitud);
} 