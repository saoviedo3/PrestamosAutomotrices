package com.banquito.sistema.analisis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.analisis.controller.dto.ObservacionAnalistaDTO;
import com.banquito.sistema.analisis.controller.dto.ObservacionDecisionDTO;
import com.banquito.sistema.analisis.controller.mapper.ObservacionAnalistaMapper;
import com.banquito.sistema.analisis.model.ObservacionAnalista;
import com.banquito.sistema.analisis.service.ObservacionAnalistaService;
import com.banquito.sistema.analisis.service.exception.AnalisisException;
import com.banquito.sistema.originacion.service.exception.CreditoException;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/observaciones-analista")
public class ObservacionAnalistaController {

    private static final Logger LOG = LoggerFactory.getLogger(ObservacionAnalistaController.class);
    private final ObservacionAnalistaService service;
    private final ObservacionAnalistaMapper mapper;

    public ObservacionAnalistaController(ObservacionAnalistaService service, ObservacionAnalistaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ObservacionAnalistaDTO>> listarTodas() {
        LOG.info("Obteniendo todas las observaciones de analistas");
        List<ObservacionAnalista> observaciones = this.service.findAll();
        List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
        
        for (ObservacionAnalista observacion : observaciones) {
            dtos.add(mapper.toDTO(observacion));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservacionAnalistaDTO> obtenerPorId(@PathVariable Integer id) {
        LOG.info("Obteniendo observación de analista con ID: {}", id);
        try {
            ObservacionAnalista observacion = this.service.findById(id);
            return ResponseEntity.ok(this.mapper.toDTO(observacion));
        } catch (AnalisisException e) {
            LOG.error("Error al obtener observación de analista por ID: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<ObservacionAnalistaDTO>> listarPorSolicitud(@PathVariable Integer idSolicitud) {
        LOG.info("Obteniendo observaciones de analistas para la solicitud con ID: {}", idSolicitud);
        List<ObservacionAnalista> observaciones = this.service.findByIdSolicitud(idSolicitud);
        List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
        
        for (ObservacionAnalista observacion : observaciones) {
            dtos.add(mapper.toDTO(observacion));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<ObservacionAnalistaDTO>> listarPorUsuario(@PathVariable String usuario) {
        LOG.info("Obteniendo observaciones de analistas para el usuario: {}", usuario);
        List<ObservacionAnalista> observaciones = this.service.findByUsuario(usuario);
        List<ObservacionAnalistaDTO> dtos = new ArrayList<>(observaciones.size());
        
        for (ObservacionAnalista observacion : observaciones) {
            dtos.add(mapper.toDTO(observacion));
        }
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ObservacionAnalistaDTO> crear(@Valid @RequestBody ObservacionAnalistaDTO observacionDTO) {
        LOG.info("Creando nueva observación de analista para la solicitud con ID: {}", observacionDTO.getIdSolicitud());
        try {
            ObservacionAnalista observacion = this.mapper.toModel(observacionDTO);
            observacion = this.service.save(observacion);
            return ResponseEntity.ok(this.mapper.toDTO(observacion));
        } catch (AnalisisException e) {
            LOG.error("Error al crear observación de analista: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para registrar una decisión del analista sobre una solicitud de crédito
     * La decisión (Aprobada/Rechazada) cambiará el estado de la solicitud
     */
    @PostMapping("/decision")
    public ResponseEntity<ObservacionAnalistaDTO> registrarDecision(@Valid @RequestBody ObservacionDecisionDTO decisionDTO) {
        LOG.info("Registrando decisión {} para la solicitud con ID: {}", 
                decisionDTO.getDecision(), decisionDTO.getObservacion().getIdSolicitud());
        try {
            ObservacionAnalista observacion = this.mapper.toModel(decisionDTO.getObservacion());
            observacion = this.service.saveConDecision(observacion, decisionDTO.getDecision());
            return ResponseEntity.ok(this.mapper.toDTO(observacion));
        } catch (AnalisisException e) {
            LOG.error("Error al registrar decisión del analista: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObservacionAnalistaDTO> actualizar(@PathVariable Integer id, 
                                                         @Valid @RequestBody ObservacionAnalistaDTO observacionDTO) {
        LOG.info("Actualizando observación de analista con ID: {}", id);
        try {
            ObservacionAnalista observacionExistente = this.service.findById(id);
            ObservacionAnalista observacionActualizar = this.mapper.toModel(observacionDTO);
            observacionActualizar.setId(id);
            observacionActualizar = this.service.update(observacionActualizar);
            return ResponseEntity.ok(this.mapper.toDTO(observacionActualizar));
        } catch (AnalisisException e) {
            LOG.error("Error al actualizar observación de analista: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        LOG.info("Eliminando observación de analista con ID: {}", id);
        try {
            this.service.delete(id);
            return ResponseEntity.ok().build();
        } catch (AnalisisException e) {
            LOG.error("Error al eliminar observación de analista: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Manejador de excepciones para AnalisisException
     * @param e Excepción lanzada
     * @return Respuesta HTTP con el mensaje de error
     */
    @ExceptionHandler({AnalisisException.class})
    public ResponseEntity<String> handleAnalisisException(AnalisisException e) {
        LOG.error("Error en el análisis: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    
    /**
     * Manejador de excepciones para CreditoException
     * @param e Excepción lanzada
     * @return Respuesta HTTP con el mensaje de error
     */
    @ExceptionHandler({CreditoException.class})
    public ResponseEntity<String> handleCreditoException(CreditoException e) {
        LOG.error("Error en la solicitud: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
} 