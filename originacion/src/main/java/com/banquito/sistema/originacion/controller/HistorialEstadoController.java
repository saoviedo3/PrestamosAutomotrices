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
@RequestMapping("/v1/historial-estados")
public class HistorialEstadoController {

    private final HistorialEstadoService service;

    public HistorialEstadoController(HistorialEstadoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HistorialEstado>> getAllHistorialEstados() {
        List<HistorialEstado> historiales = this.service.findAll();
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