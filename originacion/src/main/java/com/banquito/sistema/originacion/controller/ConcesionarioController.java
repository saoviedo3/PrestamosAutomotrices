package com.banquito.sistema.originacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public ConcesionarioController(ConcesionarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Concesionario>> getAllConcesionarios() {
        List<Concesionario> concesionarios = this.service.findAll();
        return ResponseEntity.ok(concesionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concesionario> getConcesionarioById(@PathVariable("id") Long id) {
        try {
            Concesionario concesionario = this.service.findById(id);
            return ResponseEntity.ok(concesionario);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping
    public ResponseEntity<Concesionario> createConcesionario(@Valid @RequestBody Concesionario concesionario) {
        try {
            Concesionario savedConcesionario = this.service.create(concesionario);
            return ResponseEntity.ok(savedConcesionario);
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concesionario> updateConcesionario(
            @PathVariable("id") Long id, 
            @Valid @RequestBody Concesionario concesionario) {
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