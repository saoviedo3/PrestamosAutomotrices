package com.banquito.sistema.analisis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.sistema.analisis.model.DecisionAnalista;
import com.banquito.sistema.analisis.model.ObservacionAnalista;
import com.banquito.sistema.analisis.service.ObservacionAnalistaService;
import com.banquito.sistema.analisis.service.exception.AnalisisException;
import com.banquito.sistema.originacion.service.exception.CreditoException;

import jakarta.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/observaciones-analista")
public class ObservacionAnalistaController {

    private static final Logger LOG = LoggerFactory.getLogger(ObservacionAnalistaController.class);
    private final ObservacionAnalistaService service;

    public ObservacionAnalistaController(ObservacionAnalistaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ObservacionAnalista>> listarTodas() {
        LOG.info("Obteniendo todas las observaciones de analistas");
        List<ObservacionAnalista> observaciones = this.service.findAll();
        return ResponseEntity.ok(observaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservacionAnalista> obtenerPorId(@PathVariable Long id) {
        LOG.info("Obteniendo observación de analista con ID: {}", id);
        try {
            ObservacionAnalista observacion = this.service.findById(id);
            return ResponseEntity.ok(observacion);
        } catch (AnalisisException e) {
            LOG.error("Error al obtener observación de analista por ID: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<ObservacionAnalista>> listarPorSolicitud(@PathVariable Long idSolicitud) {
        LOG.info("Obteniendo observaciones de analistas para la solicitud con ID: {}", idSolicitud);
        List<ObservacionAnalista> observaciones = this.service.findByIdSolicitud(idSolicitud);
        return ResponseEntity.ok(observaciones);
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<ObservacionAnalista>> listarPorUsuario(@PathVariable String usuario) {
        LOG.info("Obteniendo observaciones de analistas para el usuario: {}", usuario);
        List<ObservacionAnalista> observaciones = this.service.findByUsuario(usuario);
        return ResponseEntity.ok(observaciones);
    }

    @PostMapping
    public ResponseEntity<ObservacionAnalista> crear(@Valid @RequestBody ObservacionAnalista observacion) {
        LOG.info("Creando nueva observación de analista para la solicitud con ID: {}", observacion.getIdSolicitud());
        try {
            observacion = this.service.save(observacion);
            return ResponseEntity.ok(observacion);
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
    public ResponseEntity<ObservacionAnalista> registrarDecision(@Valid @RequestBody DecisionAnalista decision) {
        LOG.info("Registrando decisión {} para la solicitud con ID: {}", 
                decision.getDecision(), decision.getObservacion().getIdSolicitud());
        try {
            ObservacionAnalista observacion = this.service.saveConDecision(decision.getObservacion(), decision.getDecision());
            return ResponseEntity.ok(observacion);
        } catch (AnalisisException e) {
            LOG.error("Error al registrar decisión del analista: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObservacionAnalista> actualizar(@PathVariable Long id, 
                                                       @Valid @RequestBody ObservacionAnalista observacion) {
        LOG.info("Actualizando observación de analista con ID: {}", id);
        try {
            this.service.findById(id); // Verificar que exista
            observacion.setId(id);
            observacion = this.service.update(observacion);
            return ResponseEntity.ok(observacion);
        } catch (AnalisisException e) {
            LOG.error("Error al actualizar observación de analista: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
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