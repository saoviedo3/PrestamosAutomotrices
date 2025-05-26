package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.service.PagareService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagares")
public class PagareController {

    private final PagareService service;

    public PagareController(PagareService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Pagare>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagare> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<Pagare>> getBySolicitudId(@PathVariable Long idSolicitud) {
        return ResponseEntity.ok(service.getBySolicitudId(idSolicitud));
    }

    @PostMapping
    public ResponseEntity<Pagare> create(@RequestBody Pagare pagare) {
        Pagare created = service.create(pagare);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Pagare>> createAll(@RequestBody List<Pagare> pagares) {
        List<Pagare> created = service.createAll(pagares);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
} 