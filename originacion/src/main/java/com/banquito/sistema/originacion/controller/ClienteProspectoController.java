package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.service.ClienteProspectoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/clientes-prospecto")
public class ClienteProspectoController {

    private final ClienteProspectoService service;

    public ClienteProspectoController(ClienteProspectoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClienteProspecto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteProspecto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<ClienteProspecto> getByCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(service.getByCedula(cedula));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ClienteProspecto>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteProspecto>> searchByNombreOrApellido(@RequestParam String termino) {
        return ResponseEntity.ok(service.searchByNombreOrApellido(termino));
    }

    @PostMapping
    public ResponseEntity<ClienteProspecto> create(@RequestBody ClienteProspecto cliente) {
        ClienteProspecto created = service.create(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteProspecto> update(@PathVariable Long id, @RequestBody ClienteProspecto cliente) {
        return ResponseEntity.ok(service.update(id, cliente));
    }

    /**
     * Analiza la capacidad de pago del cliente para un monto específico
     */
    @GetMapping("/{id}/analisis-capacidad")
    public ResponseEntity<ClienteProspectoService.AnalisisCapacidadPago> analizarCapacidadPago(
            @PathVariable Long id,
            @RequestParam BigDecimal montoSolicitado,
            @RequestParam Integer plazoMeses) {
        return ResponseEntity.ok(service.analizarCapacidadPago(id, montoSolicitado, plazoMeses));
    }

    /**
     * Obtiene clientes pre-aprobados para préstamos automotrices
     */
    @GetMapping("/pre-aprobados")
    public ResponseEntity<List<ClienteProspecto>> getClientesPreAprobados() {
        return ResponseEntity.ok(service.getClientesPreAprobados());
    }
} 