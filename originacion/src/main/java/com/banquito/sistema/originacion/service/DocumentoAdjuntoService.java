package com.banquito.sistema.originacion.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.originacion.exception.DocumentoAdjuntoNotFoundException;
import com.banquito.sistema.originacion.exception.InvalidDocumentTypeException;
import com.banquito.sistema.originacion.model.DocumentoAdjunto;
import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.repository.DocumentoAdjuntoRepository;

@Service
public class DocumentoAdjuntoService {

    private final DocumentoAdjuntoRepository documentoAdjuntoRepository;
    private final TipoDocumentoService tipoDocumentoService;

    public DocumentoAdjuntoService(DocumentoAdjuntoRepository documentoAdjuntoRepository, 
                                   TipoDocumentoService tipoDocumentoService) {
        this.documentoAdjuntoRepository = documentoAdjuntoRepository;
        this.tipoDocumentoService = tipoDocumentoService;
    }

    public DocumentoAdjunto findById(Long id) {
        return this.documentoAdjuntoRepository.findById(id)
                .orElseThrow(() -> new DocumentoAdjuntoNotFoundException("Documento adjunto con ID: " + id + " no encontrado"));
    }

    public List<DocumentoAdjunto> findBySolicitudId(Long idSolicitud) {
        return this.documentoAdjuntoRepository.findByIdSolicitudOrderByFechaCargadoDesc(idSolicitud);
    }

    /**
     * Adjunta un documento a una solicitud
     * Regla de negocio: Validar que el tipo de documento esté activo y sea válido
     */
    @Transactional
    public DocumentoAdjunto adjuntarDocumento(Long idSolicitud, Long idTipoDocumento, String rutaArchivo) {
        // Validar que el tipo de documento existe y está activo
        TipoDocumento tipoDocumento = this.tipoDocumentoService.findById(idTipoDocumento);
        
        if (!"Activo".equals(tipoDocumento.getEstado())) {
            throw new InvalidDocumentTypeException("El tipo de documento '" + tipoDocumento.getNombre() + "' no está activo");
        }

        // Crear el documento adjunto
        DocumentoAdjunto documento = new DocumentoAdjunto();
        documento.setIdSolicitud(idSolicitud);
        documento.setIdTipoDocumento(idTipoDocumento);
        documento.setRutaArchivo(rutaArchivo);
        documento.setFechaCargado(LocalDateTime.now());

        return this.documentoAdjuntoRepository.save(documento);
    }

    /**
     * Valida si todos los documentos obligatorios están adjuntos para una solicitud
     */
    public boolean validarDocumentosObligatorios(Long idSolicitud) {
        // Obtener todos los tipos de documentos obligatorios
        List<TipoDocumento> tiposObligatorios = this.tipoDocumentoService.findDocumentosObligatorios();
        
        // Verificar que exista al menos un documento de cada tipo obligatorio
        for (TipoDocumento tipo : tiposObligatorios) {
            boolean existeDocumento = this.documentoAdjuntoRepository
                    .existsByIdSolicitudAndIdTipoDocumento(idSolicitud, tipo.getId());
            
            if (!existeDocumento) {
                return false; // Falta documento obligatorio
            }
        }
        
        return true; // Todos los documentos obligatorios están presentes
    }

    /**
     * Obtiene la lista de documentos faltantes para una solicitud
     */
    public List<TipoDocumento> getDocumentosFaltantes(Long idSolicitud) {
        List<TipoDocumento> tiposObligatorios = this.tipoDocumentoService.findDocumentosObligatorios();
        
        return tiposObligatorios.stream()
                .filter(tipo -> !this.documentoAdjuntoRepository
                        .existsByIdSolicitudAndIdTipoDocumento(idSolicitud, tipo.getId()))
                .toList();
    }

    /**
     * Cuenta el total de documentos adjuntos para una solicitud
     */
    public Long contarDocumentosPorSolicitud(Long idSolicitud) {
        return this.documentoAdjuntoRepository.countByIdSolicitud(idSolicitud);
    }

    /**
     * Elimina un documento adjunto
     */
    @Transactional
    public void eliminarDocumento(Long documentoId) {
        DocumentoAdjunto documento = findById(documentoId);
        this.documentoAdjuntoRepository.delete(documento);
    }

    /**
     * Busca documentos por tipo específico
     */
    public List<DocumentoAdjunto> findByTipoDocumento(Long idTipoDocumento) {
        return this.documentoAdjuntoRepository.findByIdTipoDocumento(idTipoDocumento);
    }

    /**
     * Busca documentos en un rango de fechas
     */
    public List<DocumentoAdjunto> findByFechaRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return this.documentoAdjuntoRepository.findByFechaCargadoBetween(fechaInicio, fechaFin);
    }

    /**
     * Valida el formato del archivo según el tipo de documento
     */
    public boolean validarFormatoArchivo(String rutaArchivo, String tipoDocumento) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return false;
        }

        String extension = obtenerExtension(rutaArchivo).toLowerCase();
        
        // Definir extensiones permitidas según el tipo de documento
        switch (tipoDocumento.toLowerCase()) {
            case "cedula_identidad":
            case "licencia_conducir":
            case "comprobante_ingresos":
                return extension.equals("pdf") || extension.equals("jpg") || 
                       extension.equals("jpeg") || extension.equals("png");
            case "contrato_firmado":
            case "pagare":
                return extension.equals("pdf");
            default:
                return extension.equals("pdf") || extension.equals("jpg") || 
                       extension.equals("jpeg") || extension.equals("png");
        }
    }

    private String obtenerExtension(String rutaArchivo) {
        int ultimoPunto = rutaArchivo.lastIndexOf('.');
        if (ultimoPunto > 0 && ultimoPunto < rutaArchivo.length() - 1) {
            return rutaArchivo.substring(ultimoPunto + 1);
        }
        return "";
    }

    /**
     * Método específico para el módulo de originación
     */
    public boolean tieneTodosLosDocumentosRequeridos(Long idSolicitud) {
        return validarDocumentosObligatorios(idSolicitud);
    }
} 