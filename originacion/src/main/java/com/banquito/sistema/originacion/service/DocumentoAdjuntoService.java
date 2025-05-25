package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.DocumentoAdjunto;
import com.banquito.sistema.originacion.repository.DocumentoAdjuntoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentoAdjuntoService {

    private final DocumentoAdjuntoRepository documentoAdjuntoRepository;
    private final TipoDocumentoService tipoDocumentoService;

    // Extensiones de archivo permitidas
    private static final String[] EXTENSIONES_PERMITIDAS = {".pdf", ".jpg", ".jpeg", ".png", ".doc", ".docx"};
    
    /**
     * Adjuntar documento a una solicitud
     */
    public DocumentoAdjunto adjuntarDocumento(DocumentoAdjunto documento) {
        try {
            validarDocumentoAdjunto(documento);
            validarTipoDocumento(documento.getIdTipoDocumento());
            validarDocumentoDuplicado(documento.getIdSolicitud(), documento.getIdTipoDocumento());
            validarTamanoArchivo(documento.getRutaArchivo());
            
            documento.setFechaCargado(LocalDateTime.now());
            
            return documentoAdjuntoRepository.save(documento);
        } catch (Exception e) {
            throw new BusinessException("Error al adjuntar documento: " + e.getMessage(), "ADJUNTAR_DOCUMENTO");
        }
    }

    /**
     * Actualizar documento adjunto existente
     */
    public DocumentoAdjunto actualizarDocumento(Integer id, DocumentoAdjunto documentoActualizado) {
        try {
            DocumentoAdjunto documentoExistente = buscarPorId(id);
            
            validarDocumentoAdjunto(documentoActualizado);
            
            // Verificar si cambió el tipo de documento
            if (!documentoExistente.getIdTipoDocumento().equals(documentoActualizado.getIdTipoDocumento())) {
                validarTipoDocumento(documentoActualizado.getIdTipoDocumento());
                validarDocumentoDuplicado(documentoExistente.getIdSolicitud(), documentoActualizado.getIdTipoDocumento());
            }
            
            documentoExistente.setIdTipoDocumento(documentoActualizado.getIdTipoDocumento());
            documentoExistente.setRutaArchivo(documentoActualizado.getRutaArchivo());
            documentoExistente.setFechaCargado(LocalDateTime.now()); // Actualizar fecha de carga
            
            return documentoAdjuntoRepository.save(documentoExistente);
        } catch (Exception e) {
            throw new BusinessException("Error al actualizar documento: " + e.getMessage(), "ACTUALIZAR_DOCUMENTO");
        }
    }

    /**
     * Buscar documento por ID
     */
    @Transactional(readOnly = true)
    public DocumentoAdjunto buscarPorId(Integer id) {
        return documentoAdjuntoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "DocumentoAdjunto"));
    }

    /**
     * Listar documentos por solicitud
     */
    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> listarPorSolicitud(Integer idSolicitud) {
        return documentoAdjuntoRepository.findBySolicitudCredito_IdSolicitud(idSolicitud);
    }

    /**
     * Listar documentos por tipo
     */
    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> listarPorTipoDocumento(Integer idTipoDocumento) {
        return documentoAdjuntoRepository.findByTipoDocumento_IdTipoDocumento(idTipoDocumento);
    }

    /**
     * Buscar documento específico por solicitud y tipo
     */
    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> buscarPorSolicitudYTipo(Integer idSolicitud, Integer idTipoDocumento) {
        return documentoAdjuntoRepository.findBySolicitudCredito_IdSolicitudAndTipoDocumento_IdTipoDocumento(idSolicitud, idTipoDocumento);
    }

    /**
     * Eliminar documento adjunto
     */
    public void eliminarDocumento(Integer id) {
        try {
            DocumentoAdjunto documento = buscarPorId(id);
            documentoAdjuntoRepository.delete(documento);
        } catch (Exception e) {
            throw new BusinessException("Error al eliminar documento: " + e.getMessage(), "ELIMINAR_DOCUMENTO");
        }
    }

    /**
     * Eliminar todos los documentos de una solicitud
     */
    public void eliminarDocumentosPorSolicitud(Integer idSolicitud) {
        try {
            List<DocumentoAdjunto> documentos = listarPorSolicitud(idSolicitud);
            documentoAdjuntoRepository.deleteAll(documentos);
        } catch (Exception e) {
            throw new BusinessException("Error al eliminar documentos: " + e.getMessage(), "ELIMINAR_DOCUMENTOS");
        }
    }

    /**
     * Actualizar ruta de archivo
     */
    public DocumentoAdjunto actualizarRutaArchivo(Integer id, String nuevaRuta) {
        try {
            DocumentoAdjunto documento = buscarPorId(id);
            
            if (nuevaRuta == null || nuevaRuta.trim().isEmpty()) {
                throw new ValidationException("rutaArchivo", "requerida");
            }
            
            validarExtensionArchivo(nuevaRuta);
            
            documento.setRutaArchivo(nuevaRuta);
            documento.setFechaCargado(LocalDateTime.now());
            
            return documentoAdjuntoRepository.save(documento);
        } catch (Exception e) {
            throw new BusinessException("Error al actualizar ruta: " + e.getMessage(), "ACTUALIZAR_RUTA");
        }
    }

    /**
     * Verificar si existe documento para solicitud y tipo específico
     */
    @Transactional(readOnly = true)
    public boolean existeDocumentoPorSolicitudYTipo(Integer idSolicitud, Integer idTipoDocumento) {
        return documentoAdjuntoRepository.existsBySolicitudCredito_IdSolicitudAndTipoDocumento_IdTipoDocumento(idSolicitud, idTipoDocumento);
    }

    /**
     * Contar documentos por solicitud
     */
    @Transactional(readOnly = true)
    public long contarDocumentosPorSolicitud(Integer idSolicitud) {
        return documentoAdjuntoRepository.countBySolicitudCredito_IdSolicitud(idSolicitud);
    }

    /**
     * Validar completitud de documentos requeridos para una solicitud
     */
    @Transactional(readOnly = true)
    public boolean validarDocumentosCompletos(Integer idSolicitud, List<Integer> tiposRequeridos) {
        for (Integer idTipoDocumento : tiposRequeridos) {
            if (!existeDocumentoPorSolicitudYTipo(idSolicitud, idTipoDocumento)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtener tipos de documentos faltantes para una solicitud
     */
    @Transactional(readOnly = true)
    public List<Integer> obtenerTiposDocumentosFaltantes(Integer idSolicitud, List<Integer> tiposRequeridos) {
        return tiposRequeridos.stream()
                .filter(idTipo -> !existeDocumentoPorSolicitudYTipo(idSolicitud, idTipo))
                .toList();
    }

    /**
     * Reemplazar documento existente
     */
    public DocumentoAdjunto reemplazarDocumento(Integer idSolicitud, Integer idTipoDocumento, String nuevaRutaArchivo) {
        try {
            List<DocumentoAdjunto> documentosExistentes = buscarPorSolicitudYTipo(idSolicitud, idTipoDocumento);
            
            if (documentosExistentes.isEmpty()) {
                // Crear nuevo documento si no existe
                DocumentoAdjunto nuevoDocumento = new DocumentoAdjunto();
                nuevoDocumento.setIdSolicitud(idSolicitud);
                nuevoDocumento.setIdTipoDocumento(idTipoDocumento);
                nuevoDocumento.setRutaArchivo(nuevaRutaArchivo);
                return adjuntarDocumento(nuevoDocumento);
            } else {
                // Actualizar el primer documento encontrado
                DocumentoAdjunto documento = documentosExistentes.get(0);
                return actualizarRutaArchivo(documento.getIdDocumento(), nuevaRutaArchivo);
            }
        } catch (Exception e) {
            throw new BusinessException("Error al reemplazar documento: " + e.getMessage(), "REEMPLAZAR_DOCUMENTO");
        }
    }

    /**
     * Validar si el archivo es válido según la extensión
     */
    @Transactional(readOnly = true)
    public boolean esArchivoValido(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return false;
        }
        
        for (String extension : EXTENSIONES_PERMITIDAS) {
            if (nombreArchivo.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtener información de documentos con detalles del tipo
     */
    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> obtenerDocumentosConDetalles(Integer idSolicitud) {
        List<DocumentoAdjunto> documentos = listarPorSolicitud(idSolicitud);
        
        // En una implementación real, aquí podrías cargar los detalles del TipoDocumento
        // usando join fetch o cargando por separado
        for (DocumentoAdjunto documento : documentos) {
            try {
                tipoDocumentoService.buscarPorId(documento.getIdTipoDocumento());
                // Aquí podrías agregar la lógica para usar la información del tipo de documento
            } catch (Exception e) {
                // Log error pero continúa con los demás documentos
            }
        }
        
        return documentos;
    }

    /**
     * Crear copia de documentos para nueva solicitud (en caso de renovación)
     */
    public List<DocumentoAdjunto> copiarDocumentos(Integer idSolicitudOrigen, Integer idSolicitudDestino) {
        try {
            List<DocumentoAdjunto> documentosOrigen = listarPorSolicitud(idSolicitudOrigen);
            
            return documentosOrigen.stream()
                    .map(docOrigen -> {
                        DocumentoAdjunto nuevoDoc = new DocumentoAdjunto();
                        nuevoDoc.setIdSolicitud(idSolicitudDestino);
                        nuevoDoc.setIdTipoDocumento(docOrigen.getIdTipoDocumento());
                        nuevoDoc.setRutaArchivo(docOrigen.getRutaArchivo()); // En un caso real, copiarías el archivo
                        nuevoDoc.setFechaCargado(LocalDateTime.now());
                        return documentoAdjuntoRepository.save(nuevoDoc);
                    })
                    .toList();
        } catch (Exception e) {
            throw new BusinessException("Error al copiar documentos: " + e.getMessage(), "COPIAR_DOCUMENTOS");
        }
    }

    // Métodos privados de validación
    private void validarDocumentoAdjunto(DocumentoAdjunto documento) {
        if (documento.getIdSolicitud() == null) {
            throw new ValidationException("idSolicitud", "requerido");
        }
        
        if (documento.getIdTipoDocumento() == null) {
            throw new ValidationException("idTipoDocumento", "requerido");
        }
        
        if (documento.getRutaArchivo() == null || documento.getRutaArchivo().trim().isEmpty()) {
            throw new ValidationException("rutaArchivo", "requerida");
        }
        
        validarExtensionArchivo(documento.getRutaArchivo());
        validarTamanoArchivo(documento.getRutaArchivo());
    }

    private void validarExtensionArchivo(String rutaArchivo) {
        if (!esArchivoValido(rutaArchivo)) {
            throw new ValidationException("rutaArchivo", "extensión no permitida. Extensiones válidas: " + String.join(", ", EXTENSIONES_PERMITIDAS));
        }
    }

    private void validarTamanoArchivo(String rutaArchivo) {
        // Aquí implementarías la lógica para validar el tamaño del archivo
        // Por ejemplo, usando java.io.File o java.nio.file.Files
        // if (file.length() > TAMANO_MAXIMO) {
        //     throw new ValidationException("rutaArchivo", "el archivo excede el tamaño máximo permitido");
        // }
    }

    private void validarTipoDocumento(Integer idTipoDocumento) {
        try {
            tipoDocumentoService.buscarPorId(idTipoDocumento);
        } catch (NotFoundException e) {
            throw new ValidationException("idTipoDocumento", "tipo de documento no existe");
        }
    }

    private void validarDocumentoDuplicado(Integer idSolicitud, Integer idTipoDocumento) {
        if (existeDocumentoPorSolicitudYTipo(idSolicitud, idTipoDocumento)) {
            throw new BusinessException("Ya existe un documento de este tipo para la solicitud", "DOCUMENTO_DUPLICADO");
        }
    }
} 