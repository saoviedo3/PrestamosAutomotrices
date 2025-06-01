package com.banquito.sistema.originacion.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.banquito.sistema.originacion.model.HistorialEstado;
import com.banquito.sistema.originacion.service.HistorialEstadoService;

@RestController
@RequestMapping("/api/historial-estados")
public class HistorialEstadoController {

    private final HistorialEstadoService service;

    public HistorialEstadoController(HistorialEstadoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HistorialEstado>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialEstado> getHistorialEstadoById(@PathVariable Long id) {
        HistorialEstado historialEstado = service.findById(id);
        return ResponseEntity.ok(historialEstado);
    }

    @PostMapping
    public ResponseEntity<HistorialEstado> createHistorialEstado(@RequestBody HistorialEstado historialEstado) {
        HistorialEstado nuevaHistorial = service.create(historialEstado);
        return new ResponseEntity<>(nuevaHistorial, HttpStatus.CREATED);
    }

} 