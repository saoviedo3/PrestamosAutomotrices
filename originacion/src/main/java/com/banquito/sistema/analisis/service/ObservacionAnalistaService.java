package com.banquito.sistema.analisis.service;

import com.banquito.sistema.analisis.model.ObservacionAnalista;
import com.banquito.sistema.analisis.repository.ObservacionAnalistaRepository;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.DeleteEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import com.banquito.sistema.exception.UpdateEntityException;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ObservacionAnalistaService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ObservacionAnalistaService.class);
    
    // Constantes para decisiones válidas
    public static final String DECISION_APROBADA = "Aprobada";
    public static final String DECISION_RECHAZADA = "Rechazada";

    private final ObservacionAnalistaRepository observacionAnalistaRepository;
    private final SolicitudCreditoService solicitudCreditoService;

    public ObservacionAnalistaService(ObservacionAnalistaRepository observacionAnalistaRepository,
                                     SolicitudCreditoService solicitudCreditoService) {
        this.observacionAnalistaRepository = observacionAnalistaRepository;
        this.solicitudCreditoService = solicitudCreditoService;
    }

    public List<ObservacionAnalista> findAll() {
        List<ObservacionAnalista> observaciones = this.observacionAnalistaRepository.findAll();
        
        // Cargar la información de la solicitud para cada observación
        for (ObservacionAnalista obs : observaciones) {
            if (obs.getIdSolicitud() != null) {
                try {
                    SolicitudCredito solicitud = this.solicitudCreditoService.findById(obs.getIdSolicitud());
                    // Crear una versión reducida de SolicitudCredito con solo la información necesaria
                    SolicitudCredito solicitudReducida = new SolicitudCredito(solicitud.getId());
                    solicitudReducida.setNumeroSolicitud(solicitud.getNumeroSolicitud());
                    solicitudReducida.setEstado(solicitud.getEstado());
                    solicitudReducida.setVersion(solicitud.getVersion());
                    
                    obs.setSolicitudCredito(solicitudReducida);
                } catch (Exception e) {
                    LOG.warn("No se pudo cargar la solicitud asociada a la observación: {}", e.getMessage());
                }
            }
        }
        
        return observaciones;
    }

    public ObservacionAnalista findById(Long id) {
        Optional<ObservacionAnalista> observacionOpt = this.observacionAnalistaRepository.findById(id);
        if (observacionOpt.isPresent()) {
            ObservacionAnalista observacion = observacionOpt.get();
            
            // Cargar explícitamente la SolicitudCredito
            if (observacion.getIdSolicitud() != null) {
                try {
                    SolicitudCredito solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
                    // Crear una versión reducida de SolicitudCredito con solo la información necesaria
                    SolicitudCredito solicitudReducida = new SolicitudCredito(solicitud.getId());
                    solicitudReducida.setNumeroSolicitud(solicitud.getNumeroSolicitud());
                    solicitudReducida.setEstado(solicitud.getEstado());
                    solicitudReducida.setVersion(solicitud.getVersion());
                    
                    observacion.setSolicitudCredito(solicitudReducida);
                } catch (Exception e) {
                    LOG.warn("No se pudo cargar la solicitud asociada a la observación: {}", e.getMessage());
                }
            }
            
            return observacion;
        } else {
            throw new InvalidDataException("ObservacionAnalista", "No existe la observación del analista con ID: " + id);
        }
    }

    public List<ObservacionAnalista> findByIdSolicitud(Long idSolicitud) {
        List<ObservacionAnalista> observaciones = this.observacionAnalistaRepository.findByIdSolicitud(idSolicitud);
        
        // Intentar cargar la información de la solicitud para todas las observaciones
        if (idSolicitud != null && !observaciones.isEmpty()) {
            try {
                SolicitudCredito solicitud = this.solicitudCreditoService.findById(idSolicitud);
                // Crear una versión reducida de SolicitudCredito con solo la información necesaria
                SolicitudCredito solicitudReducida = new SolicitudCredito(solicitud.getId());
                solicitudReducida.setNumeroSolicitud(solicitud.getNumeroSolicitud());
                solicitudReducida.setEstado(solicitud.getEstado());
                solicitudReducida.setVersion(solicitud.getVersion());
                
                // Asignar la misma instancia de solicitud a todas las observaciones
                for (ObservacionAnalista obs : observaciones) {
                    obs.setSolicitudCredito(solicitudReducida);
                }
            } catch (Exception e) {
                LOG.warn("No se pudo cargar la solicitud asociada a las observaciones: {}", e.getMessage());
            }
        }
        
        return observaciones;
    }

    public List<ObservacionAnalista> findByUsuario(String usuario) {
        List<ObservacionAnalista> observaciones = this.observacionAnalistaRepository.findByUsuario(usuario);
        
        // Cargar la información de la solicitud para cada observación
        for (ObservacionAnalista obs : observaciones) {
            if (obs.getIdSolicitud() != null) {
                try {
                    SolicitudCredito solicitud = this.solicitudCreditoService.findById(obs.getIdSolicitud());
                    // Crear una versión reducida de SolicitudCredito con solo la información necesaria
                    SolicitudCredito solicitudReducida = new SolicitudCredito(solicitud.getId());
                    solicitudReducida.setNumeroSolicitud(solicitud.getNumeroSolicitud());
                    solicitudReducida.setEstado(solicitud.getEstado());
                    solicitudReducida.setVersion(solicitud.getVersion());
                    
                    obs.setSolicitudCredito(solicitudReducida);
                } catch (Exception e) {
                    LOG.warn("No se pudo cargar la solicitud asociada a la observación: {}", e.getMessage());
                }
            }
        }
        
        return observaciones;
    }

    @Transactional
    public ObservacionAnalista save(ObservacionAnalista observacion) {
        // Verificar que la solicitud exista
        if (observacion.getIdSolicitud() == null) {
            throw new InvalidDataException("ObservacionAnalista", "El ID de la solicitud es obligatorio");
        }
        
        // Verificar que la solicitud exista en la base de datos
        SolicitudCredito solicitud;
        try {
            solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            
            // Verificar que la solicitud esté en estado que permita observaciones (EnRevision)
            if (!"EnRevision".equals(solicitud.getEstado())) {
                throw new InvalidDataException("ObservacionAnalista", "Solo se pueden agregar observaciones a solicitudes en estado de revisión");
            }
        } catch (Exception e) {
            throw new InvalidDataException("ObservacionAnalista", "La solicitud de crédito no existe: " + e.getMessage());
        }
        
        // Establecer la fecha actual si no se proporcionó
        if (observacion.getFechaHora() == null) {
            observacion.setFechaHora(LocalDateTime.now());
        }
        
        LOG.info("Guardando observación del analista {} para la solicitud {}", 
                observacion.getUsuario(), observacion.getIdSolicitud());
        
        try {
            ObservacionAnalista observacionGuardada = this.observacionAnalistaRepository.save(observacion);
            
            // Crear una versión reducida de SolicitudCredito con solo la información necesaria
            SolicitudCredito solicitudReducida = new SolicitudCredito(solicitud.getId());
            solicitudReducida.setNumeroSolicitud(solicitud.getNumeroSolicitud());
            solicitudReducida.setEstado(solicitud.getEstado());
            solicitudReducida.setVersion(solicitud.getVersion());
            
            observacionGuardada.setSolicitudCredito(solicitudReducida);
            
            return observacionGuardada;
        } catch (Exception e) {
            throw new CreateEntityException("ObservacionAnalista", "Error al guardar la observación: " + e.getMessage());
        }
    }

    /**
     * Guarda una observación del analista y aplica la decisión a la solicitud de crédito
     * @param observacion Observación del analista
     * @param decision Decisión a aplicar ("Aprobada" o "Rechazada")
     * @return Observación guardada
     */
    @Transactional
    public ObservacionAnalista saveConDecision(ObservacionAnalista observacion, String decision) {
        // Verificar que la solicitud exista
        if (observacion.getIdSolicitud() == null) {
            throw new InvalidDataException("ObservacionAnalista", "El ID de la solicitud es obligatorio");
        }
        
        // Verificar que la decisión sea válida
        if (decision == null) {
            throw new InvalidDataException("ObservacionAnalista", "La decisión es obligatoria");
        }
        
        if (!DECISION_APROBADA.equals(decision) && !DECISION_RECHAZADA.equals(decision)) {
            throw new InvalidDataException("ObservacionAnalista", 
                    "La decisión debe ser '" + DECISION_APROBADA + "' o '" + DECISION_RECHAZADA + "'");
        }
        
        // Verificar que la solicitud exista en la base de datos
        SolicitudCredito solicitud;
        try {
            solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            
            // Verificar que la solicitud esté en estado que permita decisiones (EnRevision)
            if (!"EnRevision".equals(solicitud.getEstado())) {
                throw new InvalidDataException("ObservacionAnalista", "Solo se pueden tomar decisiones en solicitudes en estado de revisión");
            }
        } catch (Exception e) {
            throw new InvalidDataException("ObservacionAnalista", "La solicitud de crédito no existe: " + e.getMessage());
        }
        
        // Establecer la fecha actual si no se proporcionó
        if (observacion.getFechaHora() == null) {
            observacion.setFechaHora(LocalDateTime.now());
        }
        
        // Guardar la observación
        ObservacionAnalista observacionGuardada;
        try {
            observacionGuardada = this.observacionAnalistaRepository.save(observacion);
        } catch (Exception e) {
            throw new CreateEntityException("ObservacionAnalista", "Error al guardar la observación: " + e.getMessage());
        }
        
        // Cambiar el estado de la solicitud según la decisión
        LOG.info("Analista {} ha {} la solicitud {} con la razón: {}", 
                observacion.getUsuario(), 
                decision, 
                observacion.getIdSolicitud(),
                observacion.getRazonIntervencion());
        
        try {
            this.solicitudCreditoService.cambiarEstado(solicitud.getId(), decision);
            
            // Actualizar la solicitud después de cambiar el estado
            solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            
            // Crear una versión reducida de SolicitudCredito con solo la información necesaria
            SolicitudCredito solicitudReducida = new SolicitudCredito(solicitud.getId());
            solicitudReducida.setNumeroSolicitud(solicitud.getNumeroSolicitud());
            solicitudReducida.setEstado(solicitud.getEstado());
            solicitudReducida.setVersion(solicitud.getVersion());
            
            observacionGuardada.setSolicitudCredito(solicitudReducida);
        } catch (Exception e) {
            throw new UpdateEntityException("SolicitudCredito", "Error al cambiar el estado de la solicitud: " + e.getMessage());
        }
        
        return observacionGuardada;
    }

    @Transactional
    public ObservacionAnalista update(ObservacionAnalista observacion) {
        Optional<ObservacionAnalista> observacionOpt = this.observacionAnalistaRepository.findById(observacion.getId());
        if (!observacionOpt.isPresent()) {
            throw new InvalidDataException("ObservacionAnalista", "No existe la observación del analista con ID: " + observacion.getId());
        }
        
        // No permitir modificar la solicitud asociada
        ObservacionAnalista observacionExistente = observacionOpt.get();
        if (!observacionExistente.getIdSolicitud().equals(observacion.getIdSolicitud())) {
            throw new InvalidDataException("ObservacionAnalista", "No se puede cambiar la solicitud asociada a una observación");
        }
        
        // Verificar el estado de la solicitud
        SolicitudCredito solicitud;
        try {
            solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            if (!"EnRevision".equals(solicitud.getEstado())) {
                throw new InvalidDataException("ObservacionAnalista", "Solo se pueden modificar observaciones de solicitudes en estado de revisión");
            }
        } catch (Exception e) {
            throw new InvalidDataException("ObservacionAnalista", "La solicitud de crédito no existe: " + e.getMessage());
        }
        
        // Mantener la fecha original
        observacion.setFechaHora(observacionExistente.getFechaHora());
        
        // Si la versión viene null, asignar la versión de la base
        if (observacion.getVersion() == null) {
            observacion.setVersion(observacionExistente.getVersion());
        }
        
        LOG.info("Actualizando observación del analista {} para la solicitud {}", 
                observacion.getUsuario(), observacion.getIdSolicitud());
        
        try {
            ObservacionAnalista observacionActualizada = this.observacionAnalistaRepository.save(observacion);
            
            // Crear una versión reducida de SolicitudCredito con solo la información necesaria
            SolicitudCredito solicitudReducida = new SolicitudCredito(solicitud.getId());
            solicitudReducida.setNumeroSolicitud(solicitud.getNumeroSolicitud());
            solicitudReducida.setEstado(solicitud.getEstado());
            solicitudReducida.setVersion(solicitud.getVersion());
            
            observacionActualizada.setSolicitudCredito(solicitudReducida);
            
            return observacionActualizada;
        } catch (Exception e) {
            throw new UpdateEntityException("ObservacionAnalista", "Error al actualizar la observación: " + e.getMessage());
        }
    }

    @Transactional
    public void delete(Long id) {
        Optional<ObservacionAnalista> observacionOpt = this.observacionAnalistaRepository.findById(id);
        if (!observacionOpt.isPresent()) {
            throw new InvalidDataException("ObservacionAnalista", "No existe la observación del analista con ID: " + id);
        }
        
        ObservacionAnalista observacion = observacionOpt.get();
        
        // Verificar el estado de la solicitud
        try {
            SolicitudCredito solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            if (!"EnRevision".equals(solicitud.getEstado())) {
                throw new InvalidDataException("ObservacionAnalista", "Solo se pueden eliminar observaciones de solicitudes en estado de revisión");
            }
        } catch (Exception e) {
            throw new InvalidDataException("ObservacionAnalista", "La solicitud de crédito no existe: " + e.getMessage());
        }
        
        LOG.info("Eliminando observación del analista {} para la solicitud {}", 
                observacion.getUsuario(), observacion.getIdSolicitud());
        
        try {
            this.observacionAnalistaRepository.delete(observacion);
        } catch (Exception e) {
            throw new DeleteEntityException("ObservacionAnalista", "Error al eliminar la observación: " + e.getMessage());
        }
    }
} 