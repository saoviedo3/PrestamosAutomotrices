package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-credito")
public class SolicitudCreditoController {

    private static final Logger LOG = LoggerFactory.getLogger(SolicitudCreditoController.class);
    private final SolicitudCreditoService service;

    public SolicitudCreditoController(SolicitudCreditoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudCredito>> listarTodas() {
        LOG.info("Obteniendo todas las solicitudes de crédito");
        List<SolicitudCredito> solicitudes = this.service.findAll();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudCredito>> listarPorEstado(@PathVariable String estado) {
        LOG.info("Obteniendo solicitudes de crédito con estado: {}", estado);
        List<SolicitudCredito> solicitudes = this.service.findByEstado(estado);
        return ResponseEntity.ok(solicitudes);
    }

    // @GetMapping("/cliente/{idCliente}")
    // public ResponseEntity<List<SolicitudCredito>> listarPorCliente(@PathVariable Long idCliente) {
    //     LOG.info("Obteniendo solicitudes de crédito para el cliente con ID: {}", idCliente);
    //     List<SolicitudCredito> solicitudes = this.service.findByIdClienteProspecto(idCliente);
    //     return ResponseEntity.ok(solicitudes);
    // }

    // @GetMapping("/vendedor/{idVendedor}")
    // public ResponseEntity<List<SolicitudCredito>> listarPorVendedor(@PathVariable Long idVendedor) {
    //     LOG.info("Obteniendo solicitudes de crédito para el vendedor con ID: {}", idVendedor);
    //     List<SolicitudCredito> solicitudes = this.service.findByIdVendedor(idVendedor);
    //     return ResponseEntity.ok(solicitudes);
    // }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCredito> obtenerPorId(@PathVariable Long id) {
        LOG.info("Obteniendo solicitud de crédito con ID: {}", id);
        SolicitudCredito solicitud = this.service.findById(id);
        return ResponseEntity.ok(solicitud);
    }

    @GetMapping("/numero/{numeroSolicitud}")
    public ResponseEntity<SolicitudCredito> obtenerPorNumero(@PathVariable String numeroSolicitud) {
        LOG.info("Obteniendo solicitud de crédito con número: {}", numeroSolicitud);
        SolicitudCredito solicitud = this.service.findByNumeroSolicitud(numeroSolicitud);
        return ResponseEntity.ok(solicitud);
    }

    @PostMapping
    public ResponseEntity<SolicitudCredito> crear(@Valid @RequestBody SolicitudCredito solicitud) {
        LOG.info("Creando nueva solicitud de crédito con número: {}", solicitud.getNumeroSolicitud());
        solicitud = this.service.save(solicitud);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Inicia la evaluación automática de una solicitud de crédito
     */
    @PostMapping("/{id}/evaluar")
    public ResponseEntity<SolicitudCredito> evaluarSolicitud(@PathVariable Long id) {
        LOG.info("Iniciando evaluación automática de solicitud de crédito con ID: {}", id);
        SolicitudCredito solicitud = this.service.findById(id);
        solicitud = this.service.evaluarSolicitud(solicitud);
        return ResponseEntity.ok(solicitud);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCredito> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody SolicitudCredito solicitud) {
        LOG.info("Actualizando solicitud de crédito con ID: {}", id);
        this.service.findById(id); // Verificar que exista
        solicitud.setId(id);
        solicitud = this.service.update(solicitud);
        return ResponseEntity.ok(solicitud);
    }

    @PutMapping("/{id}/estado/{estado}")
    public ResponseEntity<SolicitudCredito> cambiarEstado(@PathVariable Long id,
                                                        @PathVariable String estado) {
        LOG.info("Cambiando estado de solicitud de crédito con ID: {} a {}", id, estado);
        SolicitudCredito solicitud = this.service.cambiarEstado(id, estado);
        return ResponseEntity.ok(solicitud);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        LOG.info("Eliminando solicitud de crédito con ID: {}", id);
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }
}