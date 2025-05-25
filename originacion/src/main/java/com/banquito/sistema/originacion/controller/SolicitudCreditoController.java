package com.banquito.sistema.originacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.originacion.controller.dto.SolicitudCreditoDTO;
import com.banquito.sistema.originacion.controller.mapper.SolicitudCreditoMapper;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import com.banquito.sistema.originacion.service.exception.CreditoException;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/solicitudes-credito")
public class SolicitudCreditoController {

    private static final Logger LOG = LoggerFactory.getLogger(SolicitudCreditoController.class);
    private final SolicitudCreditoService service;
    private final SolicitudCreditoMapper mapper;

    public SolicitudCreditoController(SolicitudCreditoService service, SolicitudCreditoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudCreditoDTO>> listarTodas() {
        LOG.info("Obteniendo todas las solicitudes de crédito");
        List<SolicitudCredito> solicitudes = this.service.findAll();
        List<SolicitudCreditoDTO> dtos = new ArrayList<>(solicitudes.size());
        
        for (SolicitudCredito solicitud : solicitudes) {
            dtos.add(mapper.toDTO(solicitud));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudCreditoDTO>> listarPorEstado(@PathVariable String estado) {
        LOG.info("Obteniendo solicitudes de crédito con estado: {}", estado);
        List<SolicitudCredito> solicitudes = this.service.findByEstado(estado);
        List<SolicitudCreditoDTO> dtos = new ArrayList<>(solicitudes.size());
        
        for (SolicitudCredito solicitud : solicitudes) {
            dtos.add(mapper.toDTO(solicitud));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<SolicitudCreditoDTO>> listarPorCliente(@PathVariable Integer idCliente) {
        LOG.info("Obteniendo solicitudes de crédito para el cliente con ID: {}", idCliente);
        List<SolicitudCredito> solicitudes = this.service.findByIdClienteProspecto(idCliente);
        List<SolicitudCreditoDTO> dtos = new ArrayList<>(solicitudes.size());
        
        for (SolicitudCredito solicitud : solicitudes) {
            dtos.add(mapper.toDTO(solicitud));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/vendedor/{idVendedor}")
    public ResponseEntity<List<SolicitudCreditoDTO>> listarPorVendedor(@PathVariable Integer idVendedor) {
        LOG.info("Obteniendo solicitudes de crédito para el vendedor con ID: {}", idVendedor);
        List<SolicitudCredito> solicitudes = this.service.findByIdVendedor(idVendedor);
        List<SolicitudCreditoDTO> dtos = new ArrayList<>(solicitudes.size());
        
        for (SolicitudCredito solicitud : solicitudes) {
            dtos.add(mapper.toDTO(solicitud));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCreditoDTO> obtenerPorId(@PathVariable Integer id) {
        LOG.info("Obteniendo solicitud de crédito con ID: {}", id);
        try {
            SolicitudCredito solicitud = this.service.findById(id);
            return ResponseEntity.ok(this.mapper.toDTO(solicitud));
        } catch (CreditoException e) {
            LOG.error("Error al obtener solicitud de crédito por ID: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/numero/{numeroSolicitud}")
    public ResponseEntity<SolicitudCreditoDTO> obtenerPorNumero(@PathVariable String numeroSolicitud) {
        LOG.info("Obteniendo solicitud de crédito con número: {}", numeroSolicitud);
        try {
            SolicitudCredito solicitud = this.service.findByNumeroSolicitud(numeroSolicitud);
            return ResponseEntity.ok(this.mapper.toDTO(solicitud));
        } catch (CreditoException e) {
            LOG.error("Error al obtener solicitud de crédito por número: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SolicitudCreditoDTO> crear(@Valid @RequestBody SolicitudCreditoDTO solicitudDTO) {
        LOG.info("Creando nueva solicitud de crédito con número: {}", solicitudDTO.getNumeroSolicitud());
        try {
            SolicitudCredito solicitud = this.mapper.toModel(solicitudDTO);
            solicitud = this.service.save(solicitud);
            return ResponseEntity.ok(this.mapper.toDTO(solicitud));
        } catch (CreditoException e) {
            LOG.error("Error al crear solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Inicia la evaluación automática de una solicitud de crédito
     */
    @PostMapping("/{id}/evaluar")
    public ResponseEntity<SolicitudCreditoDTO> evaluarSolicitud(@PathVariable Integer id) {
        LOG.info("Iniciando evaluación automática de solicitud de crédito con ID: {}", id);
        try {
            SolicitudCredito solicitud = this.service.findById(id);
            solicitud = this.service.evaluarSolicitud(solicitud);
            
            // Incluir información sobre la clasificación del cliente
            SolicitudCreditoDTO dto = this.mapper.toDTO(solicitud);
            String clasificacionCliente = this.service.clasificarCliente(solicitud.getScoreExterno());
            dto.setMotivo("Clasificación del cliente: " + clasificacionCliente);
            
            return ResponseEntity.ok(dto);
        } catch (CreditoException e) {
            LOG.error("Error al evaluar solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCreditoDTO> actualizar(@PathVariable Integer id, 
                                                         @Valid @RequestBody SolicitudCreditoDTO solicitudDTO) {
        LOG.info("Actualizando solicitud de crédito con ID: {}", id);
        try {
            SolicitudCredito solicitudExistente = this.service.findById(id);
            SolicitudCredito solicitudActualizar = this.mapper.toModel(solicitudDTO);
            solicitudActualizar.setId(id);
            solicitudActualizar = this.service.update(solicitudActualizar);
            return ResponseEntity.ok(this.mapper.toDTO(solicitudActualizar));
        } catch (CreditoException e) {
            LOG.error("Error al actualizar solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudCreditoDTO> cambiarEstado(@PathVariable Integer id, 
                                                           @Valid @RequestBody SolicitudCreditoDTO estadoDTO) {
        LOG.info("Cambiando estado de solicitud de crédito con ID: {} a estado: {}", id, estadoDTO.getEstado());
        try {
            SolicitudCredito solicitud = this.service.cambiarEstado(id, estadoDTO.getEstado());
            return ResponseEntity.ok(this.mapper.toDTO(solicitud));
        } catch (CreditoException e) {
            LOG.error("Error al cambiar estado de solicitud de crédito: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
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