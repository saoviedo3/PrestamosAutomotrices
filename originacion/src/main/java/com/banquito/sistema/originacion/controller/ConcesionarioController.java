package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.service.ConcesionarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/concesionarios")
public class ConcesionarioController {

    private final ConcesionarioService service;

    public ConcesionarioController(ConcesionarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Concesionario>> getAllConcesionarios() {
        List<Concesionario> concesionarios = service.findAll();
        return ResponseEntity.ok(concesionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concesionario> getConcesionarioById(@PathVariable Long id) {
        Concesionario concesionario = service.findById(id);
        return ResponseEntity.ok(concesionario);
    }

    @PostMapping
    public ResponseEntity<Concesionario> createConcesionario(@Valid @RequestBody Concesionario concesionario) {
        Concesionario nuevaConcesionario = service.create(concesionario);
        return new ResponseEntity<>(nuevaConcesionario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concesionario> updateConcesionario(@PathVariable Long id,
            @Valid @RequestBody Concesionario concesionario) {
        Concesionario concesionarioActualizado = service.update(id, concesionario);
        return ResponseEntity.ok(concesionarioActualizado);
    }
}
