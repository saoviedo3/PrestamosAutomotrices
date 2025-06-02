package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.service.TipoDocumentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-documento")
public class TipoDocumentoController {
    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoDocumento>> getAllTiposDocumento() {
        List<TipoDocumento> tiposDocumento = tipoDocumentoService.getAll();
        return ResponseEntity.ok(tiposDocumento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumento> getTipoDocumentoById(@PathVariable Long id) {
        TipoDocumento tipoDocumento = tipoDocumentoService.getById(id);
        return ResponseEntity.ok(tipoDocumento);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoDocumento> getTipoDocumentoByNombre(@PathVariable String nombre) {
        TipoDocumento tipoDocumento = tipoDocumentoService.getByNombre(nombre);
        return ResponseEntity.ok(tipoDocumento);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TipoDocumento>> getTiposDocumentoByEstado(@PathVariable String estado) {
        List<TipoDocumento> tiposDocumento = tipoDocumentoService.getByEstado(estado);
        return ResponseEntity.ok(tiposDocumento);
    }

    @PostMapping
    public ResponseEntity<TipoDocumento> createTipoDocumento(@RequestBody TipoDocumento tipoDocumento) {
        TipoDocumento nuevoTipoDocumento = tipoDocumentoService.create(tipoDocumento);
        return ResponseEntity.ok(nuevoTipoDocumento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumento> updateTipoDocumento(
            @PathVariable Long id,
            @RequestBody TipoDocumento tipoDocumento) {
        TipoDocumento tipoDocumentoActualizado = tipoDocumentoService.update(id, tipoDocumento);
        return ResponseEntity.ok(tipoDocumentoActualizado);
    }
} 