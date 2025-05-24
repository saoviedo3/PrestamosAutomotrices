package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.DocumentoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoAdjuntoRepository extends JpaRepository<DocumentoAdjunto, Integer> {

    List<DocumentoAdjunto> findByIdSolicitud(Integer idSolicitud);
    
    List<DocumentoAdjunto> findByIdTipoDocumento(Integer idTipoDocumento);
    
    List<DocumentoAdjunto> findByIdSolicitudAndIdTipoDocumento(Integer idSolicitud, Integer idTipoDocumento);
    
    boolean existsByIdSolicitudAndIdTipoDocumento(Integer idSolicitud, Integer idTipoDocumento);
    
    long countByIdSolicitud(Integer idSolicitud);
} 