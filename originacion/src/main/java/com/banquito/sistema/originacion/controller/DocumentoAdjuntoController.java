package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.DocumentoAdjunto;
import com.banquito.sistema.originacion.service.DocumentoAdjuntoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos-adjuntos")
public class DocumentoAdjuntoController {
    private final DocumentoAdjuntoService documentoAdjuntoService;

    public DocumentoAdjuntoController(DocumentoAdjuntoService documentoAdjuntoService) {
        this.documentoAdjuntoService = documentoAdjuntoService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentoAdjunto>> getAllDocumentosAdjuntos() {
        List<DocumentoAdjunto> documentos = documentoAdjuntoService.getAll();
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoAdjunto> getDocumentoAdjuntoById(@PathVariable Long id) {
        DocumentoAdjunto documento = documentoAdjuntoService.getById(id);
        return ResponseEntity.ok(documento);
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<DocumentoAdjunto>> getDocumentosBySolicitudId(@PathVariable Long idSolicitud) {
        List<DocumentoAdjunto> documentos = documentoAdjuntoService.getBySolicitudId(idSolicitud);
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/tipo-documento/{idTipoDocumento}")
    public ResponseEntity<List<DocumentoAdjunto>> getDocumentosByTipoDocumentoId(@PathVariable Long idTipoDocumento) {
        List<DocumentoAdjunto> documentos = documentoAdjuntoService.getByTipoDocumentoId(idTipoDocumento);
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/solicitud/{idSolicitud}/tipo-documento/{idTipoDocumento}")
    public ResponseEntity<List<DocumentoAdjunto>> getDocumentosBySolicitudAndTipoDocumento(
            @PathVariable Long idSolicitud,
            @PathVariable Long idTipoDocumento) {
        List<DocumentoAdjunto> documentos = documentoAdjuntoService.getBySolicitudAndTipoDocumento(idSolicitud, idTipoDocumento);
        return ResponseEntity.ok(documentos);
    }

    @PostMapping
    public ResponseEntity<DocumentoAdjunto> createDocumentoAdjunto(@RequestBody DocumentoAdjunto documento) {
        DocumentoAdjunto nuevoDocumento = documentoAdjuntoService.create(documento);
        return ResponseEntity.ok(nuevoDocumento);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<DocumentoAdjunto>> createMultipleDocumentosAdjuntos(@RequestBody List<DocumentoAdjunto> documentos) {
        List<DocumentoAdjunto> nuevosDocumentos = documentoAdjuntoService.createAll(documentos);
        return ResponseEntity.ok(nuevosDocumentos);
    }
} 