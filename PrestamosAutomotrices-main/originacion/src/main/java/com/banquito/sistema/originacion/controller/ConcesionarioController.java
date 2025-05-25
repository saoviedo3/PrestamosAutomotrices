package com.banquito.sistema.originacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.banquito.sistema.originacion.exception.DuplicateException;
import com.banquito.sistema.originacion.exception.InvalidStateException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.service.ConcesionarioService;

@RestController
@RequestMapping("/v1/concesionarios")
public class ConcesionarioController {

    private final ConcesionarioService service;

    public ConcesionarioController(ConcesionarioService service) {
        this.service = service;
    }

    @GetMapping
    @JsonView(Concesionario.Views.Public.class)
    public ResponseEntity<List<Concesionario>> getAllConcesionarios(
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
        
        return ResponseEntity.ok(concesionarios);
    }

    @GetMapping("/{id}")
    @JsonView(Concesionario.Views.Public.class)
    public ResponseEntity<Concesionario> getConcesionarioById(@PathVariable("id") Long id) {
        try {
            Concesionario concesionario = this.service.findById(id);
            return ResponseEntity.ok(concesionario);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    @JsonView(Concesionario.Views.Public.class)
    public ResponseEntity<Concesionario> getConcesionarioByCodigo(@PathVariable("codigo") String codigo) {
        try {
            Concesionario concesionario = this.service.findByCodigo(codigo);
            return ResponseEntity.ok(concesionario);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @JsonView(Concesionario.Views.Public.class)
    public ResponseEntity<Concesionario> createConcesionario(
            @Validated(Concesionario.CreateValidation.class) @RequestBody Concesionario concesionario) {
        try {
            Concesionario savedConcesionario = this.service.create(concesionario);
            return ResponseEntity.ok(savedConcesionario);
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @JsonView(Concesionario.Views.Public.class)
    public ResponseEntity<Concesionario> updateConcesionario(
            @PathVariable("id") Long id, 
            @Validated(Concesionario.UpdateValidation.class) @RequestBody Concesionario concesionario) {
        try {
            Concesionario updatedConcesionario = this.service.update(id, concesionario);
            return ResponseEntity.ok(updatedConcesionario);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    @JsonView(Concesionario.Views.Public.class)
    public ResponseEntity<Concesionario> changeState(
            @PathVariable("id") Long id,
            @RequestParam("estado") String newState,
            @RequestParam("motivo") String motivo,
            @RequestParam("usuario") String usuario) {
        try {
            Concesionario updatedConcesionario = this.service.changeState(id, newState, motivo, usuario);
            return ResponseEntity.ok(updatedConcesionario);
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