package com.banquito.sistema.originacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import com.banquito.sistema.originacion.service.exception.CreditoException;

import jakarta.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/solicitudes-credito")
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
    // public ResponseEntity<List<SolicitudCredito>> listarPorCliente(@PathVariable Integer idCliente) {
    //     LOG.info("Obteniendo solicitudes de crédito para el cliente con ID: {}", idCliente);
    //     List<SolicitudCredito> solicitudes = this.service.findByIdClienteProspecto(idCliente);
    //     return ResponseEntity.ok(solicitudes);
    // }

    // @GetMapping("/vendedor/{idVendedor}")
    // public ResponseEntity<List<SolicitudCredito>> listarPorVendedor(@PathVariable Integer idVendedor) {
    //     LOG.info("Obteniendo solicitudes de crédito para el vendedor con ID: {}", idVendedor);
    //     List<SolicitudCredito> solicitudes = this.service.findByIdVendedor(idVendedor);
    //     return ResponseEntity.ok(solicitudes);
    // }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCredito> obtenerPorId(@PathVariable Long id) {
        LOG.info("Obteniendo solicitud de crédito con ID: {}", id);
        try {
            SolicitudCredito solicitud = this.service.findById(id);
            return ResponseEntity.ok(solicitud);
        } catch (CreditoException e) {
            LOG.error("Error al obtener solicitud de crédito por ID: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/numero/{numeroSolicitud}")
    public ResponseEntity<SolicitudCredito> obtenerPorNumero(@PathVariable String numeroSolicitud) {
        LOG.info("Obteniendo solicitud de crédito con número: {}", numeroSolicitud);
        try {
            SolicitudCredito solicitud = this.service.findByNumeroSolicitud(numeroSolicitud);
            return ResponseEntity.ok(solicitud);
        } catch (CreditoException e) {
            LOG.error("Error al obtener solicitud de crédito por número: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SolicitudCredito> crear(@Valid @RequestBody SolicitudCredito solicitud) {
        LOG.info("Creando nueva solicitud de crédito con número: {}", solicitud.getNumeroSolicitud());
        try {
            solicitud = this.service.save(solicitud);
            return ResponseEntity.ok(solicitud);
        } catch (CreditoException e) {
            LOG.error("Error al crear solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Inicia la evaluación automática de una solicitud de crédito
     */
    @PostMapping("/{id}/evaluar")
    public ResponseEntity<SolicitudCredito> evaluarSolicitud(@PathVariable Long id) {
        LOG.info("Iniciando evaluación automática de solicitud de crédito con ID: {}", id);
        try {
            SolicitudCredito solicitud = this.service.findById(id);
            solicitud = this.service.evaluarSolicitud(solicitud);
            
            return ResponseEntity.ok(solicitud);
        } catch (CreditoException e) {
            LOG.error("Error al evaluar solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCredito> actualizar(@PathVariable Long id, 
                                                    @Valid @RequestBody SolicitudCredito solicitud) {
        LOG.info("Actualizando solicitud de crédito con ID: {}", id);
        try {
            this.service.findById(id); // Verificar que exista
            solicitud.setId(id);
            solicitud = this.service.update(solicitud);
            return ResponseEntity.ok(solicitud);
        } catch (CreditoException e) {
            LOG.error("Error al actualizar solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudCredito> cambiarEstado(@PathVariable Long id, 
                                                       @RequestParam String estado) {
        LOG.info("Cambiando estado de solicitud de crédito con ID: {} a estado: {}", id, estado);
        try {
            SolicitudCredito solicitud = this.service.cambiarEstado(id, estado);
            return ResponseEntity.ok(solicitud);
        } catch (CreditoException e) {
            LOG.error("Error al cambiar estado de solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        LOG.info("Eliminando solicitud de crédito con ID: {}", id);
        try {
            this.service.delete(id);
            return ResponseEntity.ok().build();
        } catch (CreditoException e) {
            LOG.error("Error al eliminar solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @ExceptionHandler({CreditoException.class})
    public ResponseEntity<String> handleCreditoException(CreditoException e) {
        LOG.error("Error en la solicitud: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
} 