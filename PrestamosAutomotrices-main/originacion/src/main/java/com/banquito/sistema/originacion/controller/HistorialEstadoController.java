package com.banquito.sistema.originacion.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.originacion.controller.dto.HistorialEstadoDTO;
import com.banquito.sistema.originacion.controller.mapper.HistorialEstadoMapper;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.HistorialEstado;
import com.banquito.sistema.originacion.service.HistorialEstadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/historial-estados")
public class HistorialEstadoController {

    private final HistorialEstadoService service;
    private final HistorialEstadoMapper mapper;

    public HistorialEstadoController(HistorialEstadoService service, HistorialEstadoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<HistorialEstadoDTO>> getAllHistorialEstados(
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
        
        List<HistorialEstadoDTO> dtos = new ArrayList<>(historiales.size());
        for (HistorialEstado historial : historiales) {
            dtos.add(mapper.toDTO(historial));
        }
        
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialEstadoDTO> getHistorialEstadoById(@PathVariable("id") Long id) {
        try {
            HistorialEstado historialEstado = this.service.findById(id);
            return ResponseEntity.ok(this.mapper.toDTO(historialEstado));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/entidad/{entidadTipo}/{entidadId}")
    public ResponseEntity<List<HistorialEstadoDTO>> getHistorialByEntidad(
            @PathVariable("entidadTipo") String entidadTipo,
            @PathVariable("entidadId") Long entidadId) {
        
        List<HistorialEstado> historiales = this.service.findByEntidadTipoAndEntidadId(entidadTipo, entidadId);
        List<HistorialEstadoDTO> dtos = new ArrayList<>(historiales.size());
        
        for (HistorialEstado historial : historiales) {
            dtos.add(mapper.toDTO(historial));
        }
        
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<HistorialEstadoDTO> createHistorialEstado(@Valid @RequestBody HistorialEstadoDTO historialEstadoDTO) {
        HistorialEstado historialEstado = this.mapper.toEntity(historialEstadoDTO);
        HistorialEstado savedHistorialEstado = this.service.create(historialEstado);
        return ResponseEntity.ok(this.mapper.toDTO(savedHistorialEstado));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.notFound().build();
    }
} 