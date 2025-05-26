package com.banquito.sistema.originacion.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.HistorialEstado;
import com.banquito.sistema.originacion.service.HistorialEstadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/historial-estados")
public class HistorialEstadoController {

    private final HistorialEstadoService service;

    public HistorialEstadoController(HistorialEstadoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HistorialEstado>> getAllHistorialEstados(
            @RequestParam(required = false) String entidadTipo,
            @RequestParam(required = false) Long entidadId,
            @RequestParam(required = false) String usuarioCambio,
            @RequestParam(required = false) String estadoNuevo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        List<HistorialEstado> historiales;
        
        if (entidadTipo != null && entidadId != null) {
            historiales = this.service.findByEntidadTipoAndEntidadId(entidadTipo, entidadId);
        } else if (entidadTipo != null) {
            historiales = this.service.findByEntidadTipo(entidadTipo);
        } else if (usuarioCambio != null) {
            historiales = this.service.findByUsuarioCambio(usuarioCambio);
        } else if (estadoNuevo != null) {
            historiales = this.service.findByEstadoNuevo(estadoNuevo);
        } else if (fechaInicio != null && fechaFin != null) {
            historiales = this.service.findByFechaCambioBetween(fechaInicio, fechaFin);
        } else {
            historiales = this.service.findAll();
        }
        
        return ResponseEntity.ok(historiales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialEstado> getHistorialEstadoById(@PathVariable("id") Long id) {
        try {
            HistorialEstado historialEstado = this.service.findById(id);
            return ResponseEntity.ok(historialEstado);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/entidad/{entidadTipo}/{entidadId}")
    public ResponseEntity<List<HistorialEstado>> getHistorialByEntidad(
            @PathVariable("entidadTipo") String entidadTipo,
            @PathVariable("entidadId") Long entidadId) {
        
        List<HistorialEstado> historiales = this.service.findByEntidadTipoAndEntidadId(entidadTipo, entidadId);
        
        return ResponseEntity.ok(historiales);
    }

    @PostMapping
    public ResponseEntity<HistorialEstado> createHistorialEstado(@Valid @RequestBody HistorialEstado historialEstado) {
        HistorialEstado savedHistorialEstado = this.service.create(historialEstado);
        return ResponseEntity.ok(savedHistorialEstado);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.notFound().build();
    }
} 