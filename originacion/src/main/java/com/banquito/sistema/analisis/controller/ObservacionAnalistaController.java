package com.banquito.sistema.analisis.controller;

import com.banquito.sistema.analisis.model.ObservacionAnalista;
import com.banquito.sistema.analisis.service.ObservacionAnalistaService;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        
        // Cargar la información de la solicitud para cada observación
        observaciones.forEach(obs -> {
            if (obs.getIdSolicitud() != null && obs.getSolicitudCredito() == null) {
                try {
                    // Intentar obtener los datos de la solicitud usando el ID
                    SolicitudCredito solicitudReducida = new SolicitudCredito(obs.getIdSolicitud());
                    // Al menos establecemos el ID para que no sea null
                    solicitudReducida.setVersion(0L); // Establecer una versión por defecto
                    obs.setSolicitudCredito(solicitudReducida);
                } catch (Exception e) {
                    LOG.warn("No se pudo cargar información mínima de la solicitud: {}", e.getMessage());
                }
            }
        });
        
        return ResponseEntity.ok(observaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservacionAnalista> obtenerPorId(@PathVariable Long id) {
        LOG.info("Obteniendo observación de analista con ID: {}", id);
        ObservacionAnalista observacion = this.service.findById(id);
        return ResponseEntity.ok(observacion);
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
        observacion = this.service.save(observacion);
        return ResponseEntity.ok(observacion);
    }

    /**
     * Endpoint para registrar una decisión del analista sobre una solicitud de crédito
     * La decisión (Aprobada/Rechazada) cambiará el estado de la solicitud
     * @param observacion La observación del analista
     * @param decision La decisión a aplicar (Aprobada o Rechazada)
     * @return La observación guardada
     */
    @PostMapping("/decision/{decision}")
    public ResponseEntity<ObservacionAnalista> registrarDecision(
            @Valid @RequestBody ObservacionAnalista observacion,
            @PathVariable String decision) {
        LOG.info("Registrando decisión {} para la solicitud con ID: {}", 
                decision, observacion.getIdSolicitud());
        ObservacionAnalista observacionGuardada = this.service.saveConDecision(observacion, decision);
        return ResponseEntity.ok(observacionGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObservacionAnalista> actualizar(@PathVariable Long id, 
                                                       @Valid @RequestBody ObservacionAnalista observacion) {
        LOG.info("Actualizando observación de analista con ID: {}", id);
        this.service.findById(id); // Verificar que exista
        observacion.setId(id);
        observacion = this.service.update(observacion);
        return ResponseEntity.ok(observacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        LOG.info("Eliminando observación de analista con ID: {}", id);
        this.service.delete(id);
        return ResponseEntity.ok().build();
    }
} 