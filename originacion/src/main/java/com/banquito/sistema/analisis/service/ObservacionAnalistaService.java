package com.banquito.sistema.analisis.service;

import com.banquito.sistema.analisis.model.ObservacionAnalista;
import com.banquito.sistema.analisis.repository.ObservacionAnalistaRepository;
import com.banquito.sistema.analisis.service.exception.AnalisisException;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import com.banquito.sistema.originacion.service.exception.CreditoException;

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

    private final ObservacionAnalistaRepository observacionAnalistaRepository;
    private final SolicitudCreditoService solicitudCreditoService;

    public ObservacionAnalistaService(ObservacionAnalistaRepository observacionAnalistaRepository,
                                     SolicitudCreditoService solicitudCreditoService) {
        this.observacionAnalistaRepository = observacionAnalistaRepository;
        this.solicitudCreditoService = solicitudCreditoService;
    }

    public List<ObservacionAnalista> findAll() {
        return this.observacionAnalistaRepository.findAll();
    }

    public ObservacionAnalista findById(Integer id) {
        Optional<ObservacionAnalista> observacionOpt = this.observacionAnalistaRepository.findById(id);
        if (observacionOpt.isPresent()) {
            return observacionOpt.get();
        } else {
            throw new AnalisisException("No existe la observación del analista con ID: " + id);
        }
    }

    public List<ObservacionAnalista> findByIdSolicitud(Integer idSolicitud) {
        return this.observacionAnalistaRepository.findByIdSolicitud(idSolicitud);
    }

    public List<ObservacionAnalista> findByUsuario(String usuario) {
        return this.observacionAnalistaRepository.findByUsuario(usuario);
    }

    @Transactional
    public ObservacionAnalista save(ObservacionAnalista observacion) {
        // Verificar que la solicitud exista
        if (observacion.getIdSolicitud() == null) {
            throw new AnalisisException("El ID de la solicitud es obligatorio");
        }
        
        // Verificar que la solicitud exista en la base de datos
        try {
            SolicitudCredito solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            
            // Verificar que la solicitud esté en estado que permita observaciones (EnRevision)
            if (!"EnRevision".equals(solicitud.getEstado())) {
                throw new AnalisisException("Solo se pueden agregar observaciones a solicitudes en estado de revisión");
            }
        } catch (CreditoException e) {
            throw new AnalisisException("La solicitud de crédito no existe: " + e.getMessage());
        }
        
        // Establecer la fecha actual si no se proporcionó
        if (observacion.getFechaHora() == null) {
            observacion.setFechaHora(LocalDateTime.now());
        }
        
        LOG.info("Guardando observación del analista {} para la solicitud {}", 
                observacion.getUsuario(), observacion.getIdSolicitud());
        
        return this.observacionAnalistaRepository.save(observacion);
    }

    /**
     * Guarda una observación del analista y aplica la decisión a la solicitud de crédito
     * @param observacion Observación del analista
     * @param decision Decisión a aplicar ("Aprobada", "Rechazada")
     * @return Observación guardada
     */
    @Transactional
    public ObservacionAnalista saveConDecision(ObservacionAnalista observacion, String decision) {
        // Verificar que la solicitud exista
        if (observacion.getIdSolicitud() == null) {
            throw new AnalisisException("El ID de la solicitud es obligatorio");
        }
        
        // Verificar que la decisión sea válida
        if (!"Aprobada".equals(decision) && !"Rechazada".equals(decision)) {
            throw new AnalisisException("La decisión debe ser 'Aprobada' o 'Rechazada'");
        }
        
        // Verificar que la solicitud exista en la base de datos
        SolicitudCredito solicitud;
        try {
            solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            
            // Verificar que la solicitud esté en estado que permita decisiones (EnRevision)
            if (!"EnRevision".equals(solicitud.getEstado())) {
                throw new AnalisisException("Solo se pueden tomar decisiones en solicitudes en estado de revisión");
            }
        } catch (CreditoException e) {
            throw new AnalisisException("La solicitud de crédito no existe: " + e.getMessage());
        }
        
        // Establecer la fecha actual si no se proporcionó
        if (observacion.getFechaHora() == null) {
            observacion.setFechaHora(LocalDateTime.now());
        }
        
        // Guardar la observación
        ObservacionAnalista observacionGuardada = this.observacionAnalistaRepository.save(observacion);
        
        // Cambiar el estado de la solicitud según la decisión
        LOG.info("Analista {} ha {} la solicitud {} con la razón: {}", 
                observacion.getUsuario(), 
                decision, 
                observacion.getIdSolicitud(),
                observacion.getRazonIntervencion());
        
        try {
            this.solicitudCreditoService.cambiarEstado(solicitud.getId(), decision);
        } catch (CreditoException e) {
            throw new AnalisisException("Error al cambiar el estado de la solicitud: " + e.getMessage());
        }
        
        return observacionGuardada;
    }

    @Transactional
    public ObservacionAnalista update(ObservacionAnalista observacion) {
        Optional<ObservacionAnalista> observacionOpt = this.observacionAnalistaRepository.findById(observacion.getId());
        if (!observacionOpt.isPresent()) {
            throw new AnalisisException("No existe la observación del analista con ID: " + observacion.getId());
        }
        
        // No permitir modificar la solicitud asociada
        ObservacionAnalista observacionExistente = observacionOpt.get();
        if (!observacionExistente.getIdSolicitud().equals(observacion.getIdSolicitud())) {
            throw new AnalisisException("No se puede cambiar la solicitud asociada a una observación");
        }
        
        // Verificar el estado de la solicitud
        try {
            SolicitudCredito solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            if (!"EnRevision".equals(solicitud.getEstado())) {
                throw new AnalisisException("Solo se pueden modificar observaciones de solicitudes en estado de revisión");
            }
        } catch (CreditoException e) {
            throw new AnalisisException("La solicitud de crédito no existe: " + e.getMessage());
        }
        
        // Mantener la fecha original
        observacion.setFechaHora(observacionExistente.getFechaHora());
        
        LOG.info("Actualizando observación del analista {} para la solicitud {}", 
                observacion.getUsuario(), observacion.getIdSolicitud());
        
        return this.observacionAnalistaRepository.save(observacion);
    }

    @Transactional
    public void delete(Integer id) {
        Optional<ObservacionAnalista> observacionOpt = this.observacionAnalistaRepository.findById(id);
        if (!observacionOpt.isPresent()) {
            throw new AnalisisException("No existe la observación del analista con ID: " + id);
        }
        
        ObservacionAnalista observacion = observacionOpt.get();
        
        // Verificar el estado de la solicitud
        try {
            SolicitudCredito solicitud = this.solicitudCreditoService.findById(observacion.getIdSolicitud());
            if (!"EnRevision".equals(solicitud.getEstado())) {
                throw new AnalisisException("Solo se pueden eliminar observaciones de solicitudes en estado de revisión");
            }
        } catch (CreditoException e) {
            throw new AnalisisException("La solicitud de crédito no existe: " + e.getMessage());
        }
        
        LOG.info("Eliminando observación del analista {} para la solicitud {}", 
                observacion.getUsuario(), observacion.getIdSolicitud());
        
        this.observacionAnalistaRepository.delete(observacion);
    }
} 