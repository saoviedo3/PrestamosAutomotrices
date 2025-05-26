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
    public List<HistorialEstado> findByIdSolicitud(Long idSolicitud) {
        return this.repository.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findByUsuario(String usuario) {
        return this.repository.findByUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findByFechaHoraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return this.repository.findByFechaHoraBetween(fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstado> findByEstado(String estado) {
        return this.repository.findByEstado(estado);
    }

    public HistorialEstado create(HistorialEstado historialEstado) {
        return this.repository.save(historialEstado);
    }

    public HistorialEstado registrarCambioEstado(Long idSolicitud, String estado, 
                                                String motivo, String usuario) {
        HistorialEstado historial = new HistorialEstado();
        historial.setIdSolicitud(idSolicitud);
        historial.setEstado(estado);
        historial.setMotivo(motivo);
        historial.setUsuario(usuario);
        historial.setFechaHora(null);
        return this.repository.save(historial);
    }
} 