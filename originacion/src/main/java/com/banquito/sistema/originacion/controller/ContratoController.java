package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.service.ContratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> getAllContratos() {
        List<Contrato> contratos = contratoService.getAll();
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> getContratoById(@PathVariable Long id) {
        Contrato contrato = contratoService.getById(id);
        return ResponseEntity.ok(contrato);
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<Contrato> getContratoBySolicitudId(@PathVariable Long idSolicitud) {
        Contrato contrato = contratoService.getBySolicitudId(idSolicitud);
        return ResponseEntity.ok(contrato);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Contrato>> getContratosByEstado(@PathVariable String estado) {
        List<Contrato> contratos = contratoService.getByEstado(estado);
        return ResponseEntity.ok(contratos);
    }

    @PostMapping
    public ResponseEntity<Contrato> createContrato(@RequestBody Contrato contrato) {
        Contrato nuevoContrato = contratoService.create(contrato);
        return ResponseEntity.ok(nuevoContrato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrato> updateContrato(
            @PathVariable Long id,
            @RequestBody Contrato contrato) {
        Contrato contratoActualizado = contratoService.update(id, contrato);
        return ResponseEntity.ok(contratoActualizado);
    }
} 