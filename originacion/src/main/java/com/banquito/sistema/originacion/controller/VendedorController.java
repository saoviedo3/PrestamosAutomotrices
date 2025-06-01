package com.banquito.sistema.originacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.banquito.sistema.originacion.model.Vendedor;
import com.banquito.sistema.originacion.service.VendedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    private final VendedorService service;

    public VendedorController(VendedorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Vendedor>> getAllVendedores() {
        List<Vendedor> vendedores = this.service.findAll();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendedor> getVendedorById(@PathVariable("id") Long id) {
        Vendedor vendedor = this.service.findById(id);
        return ResponseEntity.ok(vendedor);
    }

    @PostMapping
    public ResponseEntity<Vendedor> createVendedor(@Valid @RequestBody Vendedor vendedor) {
        Vendedor savedVendedor = this.service.create(vendedor);
        return new ResponseEntity<>(savedVendedor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> updateVendedor(
            @PathVariable("id") Long id, 
            @Valid @RequestBody Vendedor vendedor) {
        Vendedor updatedVendedor = this.service.update(id, vendedor);
        return ResponseEntity.ok(updatedVendedor);
    }

} 