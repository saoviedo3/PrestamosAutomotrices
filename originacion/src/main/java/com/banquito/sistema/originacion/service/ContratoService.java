package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.repository.ContratoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.originacion.exception.ContratoNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContratoService {
    private final ContratoRepository contratoRepository;
    private final SolicitudCreditoService solicitudService;

    public ContratoService(ContratoRepository contratoRepository,
                         SolicitudCreditoService solicitudService) {
        this.contratoRepository = contratoRepository;
        this.solicitudService = solicitudService;
    }

    @Transactional(readOnly = true)
    public List<Contrato> getAll() {
        return contratoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Contrato getById(Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new ContratoNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Contrato getBySolicitudId(Long idSolicitud) {
        return contratoRepository.findByIdSolicitud(idSolicitud)
                .orElseThrow(() -> new ContratoNotFoundException("Solicitud: " + idSolicitud));
    }

    @Transactional(readOnly = true)
    public List<Contrato> getByEstado(String estado) {
        return contratoRepository.findByEstado(estado);
    }

    @Transactional
    public Contrato create(Contrato contrato) {
        solicitudService.getById(contrato.getIdSolicitud());

        if (contratoRepository.existsByIdSolicitud(contrato.getIdSolicitud())) {
            throw new AlreadyExistsException(
                "Contrato",
                "Ya existe un contrato para la solicitud " + contrato.getIdSolicitud()
            );
        }

        if (contrato.getFechaGenerado() == null) {
            contrato.setFechaGenerado(LocalDateTime.now());
        }

        if (contrato.getEstado() == null || contrato.getEstado().isBlank()) {
            contrato.setEstado("Draft");
        }

        try {
            return contratoRepository.save(contrato);
        } catch (Exception e) {
            throw new CreateEntityException(
                "Contrato",
                "Error al crear Contrato: " + e.getMessage()
            );
        }
    }

    @Transactional
    public Contrato update(Long id, Contrato contratoActualizado) {
        Contrato contratoExistente = getById(id);

        solicitudService.getById(contratoActualizado.getIdSolicitud());

        contratoExistente.setRutaArchivo(contratoActualizado.getRutaArchivo());
        contratoExistente.setFechaFirma(contratoActualizado.getFechaFirma());
        contratoExistente.setEstado(contratoActualizado.getEstado());
        contratoExistente.setCondicionEspecial(contratoActualizado.getCondicionEspecial());

        try {
            return contratoRepository.save(contratoExistente);
        } catch (Exception e) {
            throw new CreateEntityException(
                "Contrato",
                "Error al actualizar Contrato: " + e.getMessage()
            );
        }
    }
} 