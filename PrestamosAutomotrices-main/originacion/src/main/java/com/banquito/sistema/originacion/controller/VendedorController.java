package com.banquito.sistema.originacion.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.originacion.controller.dto.VendedorDTO;
import com.banquito.sistema.originacion.controller.mapper.VendedorMapper;
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
    private final VendedorMapper mapper;

    public VendedorController(VendedorService service, VendedorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<VendedorDTO>> getAllVendedores(
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
        
        List<VendedorDTO> dtos = new ArrayList<>(vendedores.size());
        for (Vendedor vendedor : vendedores) {
            dtos.add(mapper.toDTO(vendedor));
        }
        
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> getVendedorById(@PathVariable("id") Long id) {
        try {
            Vendedor vendedor = this.service.findById(id);
            return ResponseEntity.ok(this.mapper.toDTO(vendedor));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<VendedorDTO> getVendedorByCodigo(@PathVariable("codigo") String codigo) {
        try {
            Vendedor vendedor = this.service.findByCodigo(codigo);
            return ResponseEntity.ok(this.mapper.toDTO(vendedor));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<VendedorDTO> getVendedorByCedula(@PathVariable("cedula") String cedula) {
        try {
            Vendedor vendedor = this.service.findByCedula(cedula);
            return ResponseEntity.ok(this.mapper.toDTO(vendedor));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<VendedorDTO> createVendedor(@Valid @RequestBody VendedorDTO vendedorDTO) {
        try {
            Vendedor vendedor = this.mapper.toEntity(vendedorDTO);
            Vendedor savedVendedor = this.service.create(vendedor);
            return ResponseEntity.ok(this.mapper.toDTO(savedVendedor));
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException nfe) {
            return ResponseEntity.badRequest().build();
        } catch (InvalidStateException ise) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorDTO> updateVendedor(
            @PathVariable("id") Long id, 
            @Valid @RequestBody VendedorDTO vendedorDTO) {
        try {
            Vendedor vendedor = this.mapper.toEntity(vendedorDTO);
            Vendedor updatedVendedor = this.service.update(id, vendedor);
            return ResponseEntity.ok(this.mapper.toDTO(updatedVendedor));
        } catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (DuplicateException de) {
            return ResponseEntity.badRequest().build();
        } catch (InvalidStateException ise) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<VendedorDTO> changeState(
            @PathVariable("id") Long id,
            @RequestParam("estado") String newState,
            @RequestParam("motivo") String motivo,
            @RequestParam("usuario") String usuario) {
        try {
            Vendedor updatedVendedor = this.service.changeState(id, newState, motivo, usuario);
            return ResponseEntity.ok(this.mapper.toDTO(updatedVendedor));
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