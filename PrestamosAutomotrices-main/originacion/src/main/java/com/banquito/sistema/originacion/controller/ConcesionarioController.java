package com.banquito.sistema.originacion.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.originacion.controller.dto.ConcesionarioDTO;
import com.banquito.sistema.originacion.controller.mapper.ConcesionarioMapper;
import com.banquito.sistema.originacion.exception.DuplicateException;
import com.banquito.sistema.originacion.exception.InvalidStateException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.service.ConcesionarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/concesionarios")
public class ConcesionarioController {

    private final ConcesionarioService service;
    private final ConcesionarioMapper mapper;

    public ConcesionarioController(ConcesionarioService service, ConcesionarioMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ConcesionarioDTO>> getAllConcesionarios(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) String nombre) {
        
        List<Concesionario> concesionarios;
        
        if (estado != null) {
            concesionarios = this.service.findByEstado(estado);
        } else if (ciudad != null) {
            concesionarios = this.service.findByCiudad(ciudad);
        } else if (provincia != null) {
            concesionarios = this.service.findByProvincia(provincia);
        } else if (nombre != null) {
            concesionarios = this.service.findByNombre(nombre);
        } else {
            concesionarios = this.service.findAll();
        }
        
        List<ConcesionarioDTO> dtos = new ArrayList<>(concesionarios.size());
        for (Concesionario concesionario : concesionarios) {
            dtos.add(mapper.toDTO(concesionario));
        }
        
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcesionarioDTO> getConcesionarioById(@PathVariable("id") Long id) {
        try {
            Concesionario concesionario = this.service.findById(id);
            return ResponseEntity.ok(this.mapper.toDTO(concesionario));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ConcesionarioDTO> getConcesionarioByCodigo(@PathVariable("codigo") String codigo) {
        try {
            Concesionario concesionario = this.service.findByCodigo(codigo);
            return ResponseEntity.ok(this.mapper.toDTO(concesionario));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ConcesionarioDTO> createConcesionario(@Valid @RequestBody ConcesionarioDTO concesionarioDTO) {
        try {
            Concesionario concesionario = this.mapper.toEntity(concesionarioDTO);
            Concesionario savedConcesionario = this.service.create(concesionario);
            return ResponseEntity.ok(this.mapper.toDTO(savedConcesionario));
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConcesionarioDTO> updateConcesionario(
            @PathVariable("id") Long id, 
            @Valid @RequestBody ConcesionarioDTO concesionarioDTO) {
        try {
            Concesionario concesionario = this.mapper.toEntity(concesionarioDTO);
            Concesionario updatedConcesionario = this.service.update(id, concesionario);
            return ResponseEntity.ok(this.mapper.toDTO(updatedConcesionario));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ConcesionarioDTO> changeState(
            @PathVariable("id") Long id,
            @RequestParam("estado") String newState,
            @RequestParam("motivo") String motivo,
            @RequestParam("usuario") String usuario) {
        try {
            Concesionario updatedConcesionario = this.service.changeState(id, newState, motivo, usuario);
            return ResponseEntity.ok(this.mapper.toDTO(updatedConcesionario));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (InvalidStateException ise) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({DuplicateException.class})
    public ResponseEntity<Void> handleDuplicate() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({InvalidStateException.class})
    public ResponseEntity<Void> handleInvalidState() {
        return ResponseEntity.badRequest().build();
    }
} 