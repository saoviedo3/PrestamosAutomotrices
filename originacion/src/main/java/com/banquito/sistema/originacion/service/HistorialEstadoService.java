package com.banquito.sistema.originacion.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.model.HistorialEstado;
import com.banquito.sistema.originacion.repository.HistorialEstadoRepository;

@Service
public class HistorialEstadoService {

    private final HistorialEstadoRepository repository;

    public HistorialEstadoService(HistorialEstadoRepository repository) {
        this.repository = repository;
    }

    public List<HistorialEstado> findAll() {
        return this.repository.findAll();
    }

    public HistorialEstado findById(Long id) {
        Optional<HistorialEstado> historialEstado = this.repository.findById(id);
        if (historialEstado.isEmpty()) {
            throw new NotFoundException(id.toString(), "HistorialEstado");
        }
        return historialEstado.get();
    }

    @Transactional
    public HistorialEstado create(HistorialEstado historialEstado) {
        try {
            historialEstado.setFechaHora(Timestamp.from(Instant.now()));
            return this.repository.save(historialEstado);
        } catch (Exception e) {
            throw new CreateEntityException("HistorialEstado", e.getMessage());
        }
    }

    @Transactional
    public HistorialEstado registrarCambioEstado(Long idSolicitud, String estado, 
                                                String motivo, String usuario) {
        try {
            HistorialEstado historial = new HistorialEstado();
            historial.setIdSolicitud(idSolicitud);
            historial.setEstado(estado);
            historial.setMotivo(motivo);
            historial.setUsuario(usuario);
            historial.setFechaHora(Timestamp.from(Instant.now()));
            return this.repository.save(historial);
        } catch (Exception e) {
            throw new CreateEntityException("HistorialEstado", e.getMessage());
        }
    }
} 