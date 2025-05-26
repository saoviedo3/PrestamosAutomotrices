package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.service.ContratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService service;

    public ContratoController(ContratoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<Contrato> getBySolicitudId(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(service.getBySolicitudId(idSolicitud));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Contrato>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.getByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<Contrato> create(@RequestBody Contrato contrato) {
        Contrato created = service.create(contrato);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrato> update(@PathVariable Long id, @RequestBody Contrato contrato) {
        return ResponseEntity.ok(service.update(id, contrato));
    }
} 