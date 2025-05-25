package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Vehiculo;
import com.banquito.sistema.originacion.service.VehiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public ResponseEntity<List<Vehiculo>> getAll() {
        return ResponseEntity.ok(vehiculoService.getAllVehiculos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.getVehiculoById(id));
    }

    @PostMapping
    public ResponseEntity<Vehiculo> create(@RequestBody Vehiculo vehiculo) {
        Vehiculo created = vehiculoService.createVehiculo(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}