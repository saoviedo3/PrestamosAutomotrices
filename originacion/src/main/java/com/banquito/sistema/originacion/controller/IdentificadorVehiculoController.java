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

    /**
     * GET /api/identificadores
     * Listar todos los identificadores vehiculares.
     */

    @GetMapping
    public ResponseEntity<List<IdentificadorVehiculo>> getAll() {
        List<IdentificadorVehiculo> lista = service.getAll();
        return ResponseEntity.ok(lista);
    }

    /**
     * GET /api/identificadores/{id}
     * Obtener un identificador por su ID.
     * Si no existe, se lanza IdentificadorVehiculoNotFoundException, manejado por el Handler global.
     */

    @GetMapping("/{id}")
    public ResponseEntity<IdentificadorVehiculo> getById(@PathVariable Long id) {
        IdentificadorVehiculo resultado = service.getById(id);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<IdentificadorVehiculo> create(@RequestBody IdentificadorVehiculo entity) {
        IdentificadorVehiculo creado = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
