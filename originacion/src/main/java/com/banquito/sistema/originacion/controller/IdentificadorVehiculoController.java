package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.IdentificadorVehiculo;
import com.banquito.sistema.originacion.service.IdentificadorVehiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/identificadores")
public class IdentificadorVehiculoController {

    private final IdentificadorVehiculoService service;

    public IdentificadorVehiculoController(IdentificadorVehiculoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<IdentificadorVehiculo>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdentificadorVehiculo> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<IdentificadorVehiculo> create(@RequestBody IdentificadorVehiculo entity) {
        IdentificadorVehiculo created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
