package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.service.PagareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagares")
public class PagareController {

    private final PagareService pagareService;

    public PagareController(PagareService pagareService) {
        this.pagareService = pagareService;
    }

    @GetMapping
    public ResponseEntity<List<Pagare>> getAllPagares() {
        List<Pagare> pagares = pagareService.getAll();
        return ResponseEntity.ok(pagares);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagare> getPagareById(@PathVariable Long id) {
        Pagare pagare = pagareService.getById(id);
        return ResponseEntity.ok(pagare);
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<Pagare>> getPagaresBySolicitudId(@PathVariable Long idSolicitud) {
        List<Pagare> pagares = pagareService.getBySolicitudId(idSolicitud);
        return ResponseEntity.ok(pagares);
    }

    @PostMapping
    public ResponseEntity<Pagare> createPagare(@RequestBody Pagare pagare) {
        Pagare nuevoPagare = pagareService.create(pagare);
        return ResponseEntity.ok(nuevoPagare);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<Pagare>> createMultiplePagares(@RequestBody List<Pagare> pagares) {
        List<Pagare> nuevosPagares = pagareService.createAll(pagares);
        return ResponseEntity.ok(nuevosPagares);
    }
} 