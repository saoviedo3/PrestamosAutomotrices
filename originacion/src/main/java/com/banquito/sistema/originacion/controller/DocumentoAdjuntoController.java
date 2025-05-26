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
    public ResponseEntity<List<DocumentoAdjunto>> getAll() {
        return ResponseEntity.ok(documentoAdjuntoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoAdjunto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(documentoAdjuntoService.getById(id));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<DocumentoAdjunto>> getBySolicitudId(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(documentoAdjuntoService.getBySolicitudId(idSolicitud));
    }

    @GetMapping("/tipo-documento/{idTipoDocumento}")
    public ResponseEntity<List<DocumentoAdjunto>> getByTipoDocumentoId(@PathVariable Long idTipoDocumento) {
        return ResponseEntity.ok(documentoAdjuntoService.getByTipoDocumentoId(idTipoDocumento));
    }

    @GetMapping("/solicitud/{idSolicitud}/tipo-documento/{idTipoDocumento}")
    public ResponseEntity<List<DocumentoAdjunto>> getBySolicitudAndTipoDocumento(
            @PathVariable Long idSolicitud,
            @PathVariable Long idTipoDocumento) {
        return ResponseEntity.ok(documentoAdjuntoService.getBySolicitudAndTipoDocumento(idSolicitud, idTipoDocumento));
    }

    @PostMapping
    public ResponseEntity<DocumentoAdjunto> create(@RequestBody DocumentoAdjunto documento) {
        return ResponseEntity.ok(documentoAdjuntoService.create(documento));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<DocumentoAdjunto>> createAll(@RequestBody List<DocumentoAdjunto> documentos) {
        return ResponseEntity.ok(documentoAdjuntoService.createAll(documentos));
    }
} 