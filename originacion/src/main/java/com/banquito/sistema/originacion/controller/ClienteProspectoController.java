package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.service.ClienteProspectoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes-prospecto")
public class ClienteProspectoController {

    private final ClienteProspectoService clienteProspectoService;

    public ClienteProspectoController(ClienteProspectoService clienteProspectoService) {
        this.clienteProspectoService = clienteProspectoService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteProspecto>> getAllClientesProspecto() {
        List<ClienteProspecto> clientes = clienteProspectoService.getAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteProspecto> getClienteProspectoById(@PathVariable Long id) {
        ClienteProspecto cliente = clienteProspectoService.getById(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<ClienteProspecto> getClienteProspectoByCedula(@PathVariable String cedula) {
        ClienteProspecto cliente = clienteProspectoService.getByCedula(cedula);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ClienteProspecto>> getClientesProspectoByEstado(@PathVariable String estado) {
        List<ClienteProspecto> clientes = clienteProspectoService.getByEstado(estado);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar/{termino}")
    public ResponseEntity<List<ClienteProspecto>> searchClientesProspecto(@PathVariable String termino) {
        List<ClienteProspecto> clientes = clienteProspectoService.searchByNombreOrApellido(termino);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    public ResponseEntity<ClienteProspecto> createClienteProspecto(@RequestBody ClienteProspecto cliente) {
        ClienteProspecto nuevoCliente = clienteProspectoService.create(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteProspecto> updateClienteProspecto(
            @PathVariable Long id,
            @RequestBody ClienteProspecto cliente) {
        ClienteProspecto clienteActualizado = clienteProspectoService.update(id, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }
} 