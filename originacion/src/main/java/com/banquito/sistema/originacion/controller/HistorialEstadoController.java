package com.banquito.sistema.originacion.controller;

import java.sql.Timestamp;
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
            @RequestParam(required = false) Long idSolicitud,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp fechaFin) {

        List<HistorialEstado> historiales;
        
        if (idSolicitud != null) {
            historiales = this.service.findByIdSolicitud(idSolicitud);
        } else if (usuario != null) {
            historiales = this.service.findByUsuario(usuario);
        } else if (estado != null) {
            historiales = this.service.findByEstado(estado);
        } else if (fechaInicio != null && fechaFin != null) {
            historiales = this.service.findByFechaHoraBetween(fechaInicio, fechaFin);
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

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<HistorialEstado>> getHistorialBySolicitud(
            @PathVariable("idSolicitud") Long idSolicitud) {
        
        List<HistorialEstado> historiales = this.service.findByIdSolicitud(idSolicitud);
        
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