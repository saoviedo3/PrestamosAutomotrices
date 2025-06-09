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
        Vehiculo vehiculo = vehiculoService.getVehiculoById(id);
        return ResponseEntity.ok(vehiculo);
    }

    @PostMapping
    public ResponseEntity<Vehiculo> create(@RequestBody Vehiculo vehiculo) {
        Vehiculo created = vehiculoService.createVehiculo(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> update(
            @PathVariable Long id,
            @RequestBody Vehiculo vehiculo) {
        Vehiculo updated = vehiculoService.updateVehiculo(id, vehiculo);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/vendido")
    public ResponseEntity<Vehiculo> marcarComoVendido(@PathVariable Long id) {
        Vehiculo vendido = vehiculoService.marcarComoVendido(id);
        return ResponseEntity.ok(vendido);
    }

    @GetMapping("/estado")
    public ResponseEntity<List<Vehiculo>> getVehiculosByEstado(@RequestParam String estado) {
        List<Vehiculo> vehiculos = vehiculoService.getVehiculosByEstado(estado);
        return ResponseEntity.ok(vehiculos);
    }

    @GetMapping("/concesionario/{idConcesionario}")
    public ResponseEntity<List<Vehiculo>> getVehiculosByConcesionario(@PathVariable Long idConcesionario) {
        List<Vehiculo> vehiculos = vehiculoService.getVehiculosByConcesionario(idConcesionario);
        return ResponseEntity.ok(vehiculos);
    }

    @GetMapping("/concesionario/{idConcesionario}/count")
    public ResponseEntity<Long> getVehiculoCountByConcesionario(@PathVariable Long idConcesionario) {
        Long count = vehiculoService.getVehiculoCountByConcesionario(idConcesionario);
        return ResponseEntity.ok(count);
    }



}