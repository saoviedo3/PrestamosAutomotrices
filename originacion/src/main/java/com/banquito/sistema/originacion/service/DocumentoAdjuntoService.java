package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.DocumentoAdjunto;
import com.banquito.sistema.originacion.repository.DocumentoAdjuntoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentoAdjuntoService {
    private final DocumentoAdjuntoRepository documentoAdjuntoRepository;
    private final SolicitudCreditoService solicitudService;
    private final TipoDocumentoService tipoDocumentoService;

    public DocumentoAdjuntoService(DocumentoAdjuntoRepository documentoAdjuntoRepository,
                                 SolicitudCreditoService solicitudService,
                                 TipoDocumentoService tipoDocumentoService) {
        this.documentoAdjuntoRepository = documentoAdjuntoRepository;
        this.solicitudService = solicitudService;
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> getAll() {
        return documentoAdjuntoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DocumentoAdjunto getById(Long id) {
        return documentoAdjuntoRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("DocumentoAdjunto", "No existe un documento adjunto con el id: " + id));
    }

    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> getBySolicitudId(Long idSolicitud) {
        solicitudService.getById(idSolicitud);
        return documentoAdjuntoRepository.findByIdSolicitud(idSolicitud);
    }

    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> getByTipoDocumentoId(Long idTipoDocumento) {
        tipoDocumentoService.getById(idTipoDocumento);
        return documentoAdjuntoRepository.findByIdTipoDocumento(idTipoDocumento);
    }

    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> getBySolicitudAndTipoDocumento(Long idSolicitud, Long idTipoDocumento) {
        solicitudService.getById(idSolicitud);
        tipoDocumentoService.getById(idTipoDocumento);
        return documentoAdjuntoRepository.findByIdSolicitudAndIdTipoDocumento(idSolicitud, idTipoDocumento);
    }

    @Transactional
    public DocumentoAdjunto create(DocumentoAdjunto documento) {
        solicitudService.getById(documento.getIdSolicitud());
        tipoDocumentoService.getById(documento.getIdTipoDocumento());

        if (documentoAdjuntoRepository.existsByIdSolicitudAndIdTipoDocumento(
                documento.getIdSolicitud(), 
                documento.getIdTipoDocumento())) {
            throw new AlreadyExistsException(
                "DocumentoAdjunto",
                "Ya existe un documento del tipo " + documento.getIdTipoDocumento() + 
                " para la solicitud " + documento.getIdSolicitud()
            );
        }

        if (documento.getRutaArchivo() == null || documento.getRutaArchivo().isBlank()) {
            throw new InvalidDataException(
                "DocumentoAdjunto",
                "La ruta del archivo no puede estar vacía"
            );
        }

        if (documento.getFechaCargado() == null) {
            documento.setFechaCargado(LocalDateTime.now());
        }

        try {
            return documentoAdjuntoRepository.save(documento);
        } catch (Exception e) {
            throw new CreateEntityException(
                "DocumentoAdjunto",
                "Error al crear DocumentoAdjunto: " + e.getMessage()
            );
        }
    }

    @Transactional
    public List<DocumentoAdjunto> createAll(List<DocumentoAdjunto> documentos) {
        if (documentos == null || documentos.isEmpty()) {
            throw new InvalidDataException(
                "DocumentoAdjunto",
                "La lista de documentos no puede estar vacía"
            );
        }

            Long idSolicitud = documentos.get(0).getIdSolicitud();
            solicitudService.getById(idSolicitud);

            for (DocumentoAdjunto documento : documentos) {
                if (!documento.getIdSolicitud().equals(idSolicitud)) {
                    throw new InvalidDataException(
                        "DocumentoAdjunto",
                        "Todos los documentos deben pertenecer a la misma solicitud"
                    );
                }
                tipoDocumentoService.getById(documento.getIdTipoDocumento());
                if (documentoAdjuntoRepository.existsByIdSolicitudAndIdTipoDocumento(
                        documento.getIdSolicitud(), 
                        documento.getIdTipoDocumento())) {
                    throw new AlreadyExistsException(
                        "DocumentoAdjunto",
                        "Ya existe un documento del tipo " + documento.getIdTipoDocumento() + 
                        " para la solicitud " + documento.getIdSolicitud()
                    );
                }
            if (documento.getRutaArchivo() == null || documento.getRutaArchivo().isBlank()) {
                throw new InvalidDataException(
                    "DocumentoAdjunto",
                    "La ruta del archivo no puede estar vacía"
                );
            }
        }

        LocalDateTime now = LocalDateTime.now();
        documentos.forEach(d -> {
            if (d.getFechaCargado() == null) {
                d.setFechaCargado(now);
            }
        });

        try {
            return documentoAdjuntoRepository.saveAll(documentos);
        } catch (Exception e) {
            throw new CreateEntityException(
                "DocumentoAdjunto",
                "Error al crear DocumentosAdjuntos: " + e.getMessage()
            );
        }
    }
} 