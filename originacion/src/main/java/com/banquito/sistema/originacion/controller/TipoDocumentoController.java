package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.service.TipoDocumentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-documentos")
public class TipoDocumentoController {
    private final TipoDocumentoService service;

    public TipoDocumentoController(TipoDocumentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TipoDocumento>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumento> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoDocumento> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(service.getByNombre(nombre));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TipoDocumento>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.getByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<TipoDocumento> create(@RequestBody TipoDocumento tipoDocumento) {
        TipoDocumento created = service.create(tipoDocumento);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumento> update(@PathVariable Long id, @RequestBody TipoDocumento tipoDocumento) {
        return ResponseEntity.ok(service.update(id, tipoDocumento));
    }
} 