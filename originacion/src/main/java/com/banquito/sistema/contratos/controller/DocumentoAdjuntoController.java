package com.banquito.sistema.contratos.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.sistema.contratos.model.DocumentoAdjunto;
import com.banquito.sistema.contratos.model.TipoDocumento;
import com.banquito.sistema.contratos.service.DocumentoAdjuntoService;

@RestController
@RequestMapping("/api/documentos-adjuntos")
public class DocumentoAdjuntoController {

    private final DocumentoAdjuntoService documentoAdjuntoService;

    public DocumentoAdjuntoController(DocumentoAdjuntoService documentoAdjuntoService) {
        this.documentoAdjuntoService = documentoAdjuntoService;
    }

    /**
     * Obtiene un documento adjunto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoAdjunto> findById(@PathVariable Long id) {
        DocumentoAdjunto documento = this.documentoAdjuntoService.findById(id);
        return ResponseEntity.ok(documento);
    }

    /**
     * Obtiene todos los documentos adjuntos de una solicitud
     */
    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<DocumentoAdjunto>> findBySolicitudId(@PathVariable Long idSolicitud) {
        List<DocumentoAdjunto> documentos = this.documentoAdjuntoService.findBySolicitudId(idSolicitud);
        return ResponseEntity.ok(documentos);
    }

    /**
     * Adjunta un documento a una solicitud
     */
    @PostMapping("/adjuntar")
    public ResponseEntity<DocumentoAdjunto> adjuntarDocumento(
            @RequestParam Long idSolicitud,
            @RequestParam Long idTipoDocumento,
            @RequestParam String rutaArchivo) {
        try {
            DocumentoAdjunto documento = this.documentoAdjuntoService.adjuntarDocumento(
                idSolicitud, idTipoDocumento, rutaArchivo);
            return ResponseEntity.status(HttpStatus.CREATED).body(documento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Valida si todos los documentos obligatorios están adjuntos
     */
    @GetMapping("/validar-obligatorios/{idSolicitud}")
    public ResponseEntity<Boolean> validarDocumentosObligatorios(@PathVariable Long idSolicitud) {
        boolean completos = this.documentoAdjuntoService.validarDocumentosObligatorios(idSolicitud);
        return ResponseEntity.ok(completos);
    }

    /**
     * Obtiene la lista de documentos faltantes para una solicitud
     */
    @GetMapping("/faltantes/{idSolicitud}")
    public ResponseEntity<List<TipoDocumento>> getDocumentosFaltantes(@PathVariable Long idSolicitud) {
        List<TipoDocumento> faltantes = this.documentoAdjuntoService.getDocumentosFaltantes(idSolicitud);
        return ResponseEntity.ok(faltantes);
    }

    /**
     * Cuenta el total de documentos adjuntos para una solicitud
     */
    @GetMapping("/contar/{idSolicitud}")
    public ResponseEntity<Long> contarDocumentosPorSolicitud(@PathVariable Long idSolicitud) {
        Long total = this.documentoAdjuntoService.contarDocumentosPorSolicitud(idSolicitud);
        return ResponseEntity.ok(total);
    }

    /**
     * Elimina un documento adjunto
     */
    @DeleteMapping("/{documentoId}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable Long documentoId) {
        try {
            this.documentoAdjuntoService.eliminarDocumento(documentoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca documentos por tipo específico
     */
    @GetMapping("/tipo/{idTipoDocumento}")
    public ResponseEntity<List<DocumentoAdjunto>> findByTipoDocumento(@PathVariable Long idTipoDocumento) {
        List<DocumentoAdjunto> documentos = this.documentoAdjuntoService.findByTipoDocumento(idTipoDocumento);
        return ResponseEntity.ok(documentos);
    }

    /**
     * Busca documentos en un rango de fechas
     */
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<DocumentoAdjunto>> findByFechaRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<DocumentoAdjunto> documentos = this.documentoAdjuntoService.findByFechaRango(fechaInicio, fechaFin);
        return ResponseEntity.ok(documentos);
    }

    /**
     * Valida el formato del archivo según el tipo de documento
     */
    @GetMapping("/validar-formato")
    public ResponseEntity<Boolean> validarFormatoArchivo(
            @RequestParam String rutaArchivo,
            @RequestParam String tipoDocumento) {
        boolean valido = this.documentoAdjuntoService.validarFormatoArchivo(rutaArchivo, tipoDocumento);
        return ResponseEntity.ok(valido);
    }

    /**
     * Método específico para el módulo de originación
     */
    @GetMapping("/tiene-todos-requeridos/{idSolicitud}")
    public ResponseEntity<Boolean> tieneTodosLosDocumentosRequeridos(@PathVariable Long idSolicitud) {
        boolean tieneDocumentos = this.documentoAdjuntoService.tieneTodosLosDocumentosRequeridos(idSolicitud);
        return ResponseEntity.ok(tieneDocumentos);
    }
} 