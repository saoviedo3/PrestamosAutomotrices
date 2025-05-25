package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.DocumentoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoAdjuntoRepository extends JpaRepository<DocumentoAdjunto, Integer> {

    List<DocumentoAdjunto> findBySolicitudCredito_IdSolicitud(Integer idSolicitud);

    List<DocumentoAdjunto> findByTipoDocumento_IdTipoDocumento(Integer idTipoDocumento);

    List<DocumentoAdjunto> findBySolicitudCredito_IdSolicitudAndTipoDocumento_IdTipoDocumento(Integer idSolicitud,
            Integer idTipoDocumento);

    boolean existsBySolicitudCredito_IdSolicitudAndTipoDocumento_IdTipoDocumento(Integer idSolicitud,
            Integer idTipoDocumento);

    long countBySolicitudCredito_IdSolicitud(Integer idSolicitud);
}