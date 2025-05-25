package com.banquito.sistema.originacion.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.HistorialEstado;
import com.banquito.sistema.originacion.repository.HistorialEstadoRepository;

@Service
@Transactional
public class HistorialEstadoService {

    private final HistorialEstadoRepository repository;

    public HistorialEstadoService(HistorialEstadoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findAll() {
        return this.repository.findAll();
    }

    @Transactional(readOnly = true)
    public HistorialEstado findById(Long id) {
        Optional<HistorialEstado> historialEstado = this.repository.findById(id);
        if (historialEstado.isEmpty()) {
            throw new NotFoundException(id.toString(), "HistorialEstado");
        }
        return historialEstado.get();
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findByEntidadTipoAndEntidadId(String entidadTipo, Long entidadId) {
        return this.repository.findByEntidadTipoAndEntidadIdOrderByFechaCambioDesc(entidadTipo, entidadId);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findByEntidadTipo(String entidadTipo) {
        return this.repository.findByEntidadTipo(entidadTipo);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findByUsuarioCambio(String usuarioCambio) {
        return this.repository.findByUsuarioCambio(usuarioCambio);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findByFechaCambioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return this.repository.findByFechaCambioBetween(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findByEstadoNuevo(String estadoNuevo) {
        return this.repository.findByEstadoNuevo(estadoNuevo);
    }

    public HistorialEstado create(HistorialEstado historialEstado) {
        return this.repository.save(historialEstado);
    }

    public HistorialEstado registrarCambioEstado(String entidadTipo, Long entidadId, 
                                                String estadoAnterior, String estadoNuevo, 
                                                String motivo, String usuarioCambio) {
        return this.registrarCambioEstado(entidadTipo, entidadId, estadoAnterior, 
                                        estadoNuevo, motivo, usuarioCambio, null);
    }

    public HistorialEstado registrarCambioEstado(String entidadTipo, Long entidadId, 
                                                String estadoAnterior, String estadoNuevo, 
                                                String motivo, String usuarioCambio, 
                                                String observaciones) {
        HistorialEstado historial = new HistorialEstado();
        historial.setEntidadTipo(entidadTipo);
        historial.setEntidadId(entidadId);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setMotivo(motivo);
        historial.setUsuarioCambio(usuarioCambio);
        historial.setObservaciones(observaciones);
        
        return this.repository.save(historial);
    }
} 