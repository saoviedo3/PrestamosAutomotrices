package com.banquito.sistema.originacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.originacion.exception.DuplicateException;
import com.banquito.sistema.originacion.exception.InvalidStateException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.Vendedor;
import com.banquito.sistema.originacion.service.VendedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/vendedores")
public class VendedorController {

    private final VendedorService service;

    public VendedorController(VendedorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Vendedor>> getAllVendedores(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long concesionarioId,
            @RequestParam(required = false) String nombre) {
        
        List<Vendedor> vendedores;
        
        if (estado != null) {
            vendedores = this.service.findByEstado(estado);
        } else if (concesionarioId != null) {
            vendedores = this.service.findByConcesionarioId(concesionarioId);
        } else if (nombre != null) {
            vendedores = this.service.findByNombre(nombre);
        } else {
            vendedores = this.service.findAll();
        }
        
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendedor> getVendedorById(@PathVariable("id") Long id) {
        try {
            Vendedor vendedor = this.service.findById(id);
            return ResponseEntity.ok(vendedor);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Vendedor> getVendedorByCodigo(@PathVariable("codigo") String codigo) {
        try {
            Vendedor vendedor = this.service.findByCodigo(codigo);
            return ResponseEntity.ok(vendedor);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Vendedor> getVendedorByCedula(@PathVariable("cedula") String cedula) {
        try {
            Vendedor vendedor = this.service.findByCedula(cedula);
            return ResponseEntity.ok(vendedor);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Vendedor> createVendedor(@Valid @RequestBody Vendedor vendedor) {
        try {
            Vendedor savedVendedor = this.service.create(vendedor);
            return ResponseEntity.ok(savedVendedor);
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException nfe) {
            return ResponseEntity.badRequest().build();
        } catch (InvalidStateException ise) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> updateVendedor(
            @PathVariable("id") Long id, 
            @Valid @RequestBody Vendedor vendedor) {
        try {
            Vendedor updatedVendedor = this.service.update(id, vendedor);
            return ResponseEntity.ok(updatedVendedor);
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        } catch (InvalidStateException ise) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Vendedor> changeState(
            @PathVariable("id") Long id,
            @RequestParam("estado") String newState,
            @RequestParam("motivo") String motivo,
            @RequestParam("usuario") String usuario) {
        try {
            Vendedor updatedVendedor = this.service.changeState(id, newState, motivo, usuario);
            return ResponseEntity.ok(updatedVendedor);
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