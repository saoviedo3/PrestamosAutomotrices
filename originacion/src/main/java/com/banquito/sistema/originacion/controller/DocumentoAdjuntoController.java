package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.DocumentoAdjunto;
import com.banquito.sistema.originacion.service.DocumentoAdjuntoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos-adjuntos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocumentoAdjuntoController {

    private final DocumentoAdjuntoService documentoAdjuntoService;

    @PostMapping
    public ResponseEntity<DocumentoAdjunto> adjuntar(@RequestBody DocumentoAdjunto documento) {
        DocumentoAdjunto documentoCreado = documentoAdjuntoService.adjuntarDocumento(documento);
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoAdjunto> buscarPorId(@PathVariable Integer id) {
        DocumentoAdjunto documento = documentoAdjuntoService.buscarPorId(id);
        return ResponseEntity.ok(documento);
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<DocumentoAdjunto>> listarPorSolicitud(@PathVariable Integer idSolicitud) {
        List<DocumentoAdjunto> documentos = documentoAdjuntoService.listarPorSolicitud(idSolicitud);
        return ResponseEntity.ok(documentos);
    }

    @PutMapping("/{id}/reemplazar")
    public ResponseEntity<DocumentoAdjunto> reemplazarDocumento(@PathVariable Integer id,
            @RequestParam Integer idSolicitud,
            @RequestParam String nuevaRuta) {
        DocumentoAdjunto documento = documentoAdjuntoService.reemplazarDocumento(idSolicitud, id, nuevaRuta);
        return ResponseEntity.ok(documento);
    }

    @GetMapping("/solicitud/{idSolicitud}/validar-completos")
    public ResponseEntity<Boolean> validarDocumentosCompletos(@PathVariable Integer idSolicitud,
            @RequestParam List<Integer> idsRequeridos) {
        boolean completos = documentoAdjuntoService.validarDocumentosCompletos(idSolicitud, idsRequeridos);
        return ResponseEntity.ok(completos);
    }

    @GetMapping("/solicitud/{idSolicitud}/tipos-faltantes")
    public ResponseEntity<List<Integer>> obtenerTiposFaltantes(@PathVariable Integer idSolicitud,
            @RequestParam List<Integer> idsRequeridos) {
        List<Integer> faltantes = documentoAdjuntoService.obtenerTiposDocumentosFaltantes(idSolicitud, idsRequeridos);
        return ResponseEntity.ok(faltantes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        documentoAdjuntoService.eliminarDocumento(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<Void> eliminarPorSolicitud(@PathVariable Integer idSolicitud) {
        documentoAdjuntoService.eliminarDocumentosPorSolicitud(idSolicitud);
        return ResponseEntity.noContent().build();
    }
}
