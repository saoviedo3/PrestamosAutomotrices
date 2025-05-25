package com.banquito.sistema.auditoria.controller;

import com.banquito.sistema.auditoria.model.Auditoria;
import com.banquito.sistema.auditoria.service.AuditoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Auditoria>> getAll() {
        return ResponseEntity.ok(auditoriaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditoria> getById(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Auditoria> create(@RequestBody Auditoria auditoria) {
        Auditoria created = auditoriaService.createAuditoria(auditoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}