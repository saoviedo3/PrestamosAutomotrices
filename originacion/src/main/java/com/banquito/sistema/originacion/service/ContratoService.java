package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.repository.ContratoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import com.banquito.sistema.exception.UpdateEntityException;

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
                .orElseThrow(() -> new InvalidDataException("Contrato", "No existe un contrato con el id: " + id));
    }

    @Transactional(readOnly = true)
    public Contrato getBySolicitudId(Long idSolicitud) {
        return contratoRepository.findByIdSolicitud(idSolicitud)
                .orElseThrow(() -> new InvalidDataException("Contrato", "No existe un contrato para la solicitud: " + idSolicitud));
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

        if (contrato.getRutaArchivo() == null || contrato.getRutaArchivo().isBlank()) {
            throw new InvalidDataException(
                "Contrato",
                "La ruta del archivo no puede estar vacía"
            );
        }

        if (contrato.getFechaFirma() != null && contrato.getFechaFirma().isBefore(LocalDateTime.now())) {
            throw new InvalidDataException(
                "Contrato",
                "La fecha de firma debe ser futura"
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

        if (contratoActualizado.getRutaArchivo() == null || contratoActualizado.getRutaArchivo().isBlank()) {
            throw new InvalidDataException(
                "Contrato",
                "La ruta del archivo no puede estar vacía"
            );
        }

        if (contratoActualizado.getFechaFirma() != null && contratoActualizado.getFechaFirma().isBefore(LocalDateTime.now())) {
            throw new InvalidDataException(
                "Contrato",
                "La fecha de firma debe ser futura"
            );
        }

        if (contratoActualizado.getEstado() != null && !contratoActualizado.getEstado().isBlank()) {
            String estado = contratoActualizado.getEstado();
            if (!estado.equals("Draft") && !estado.equals("Signed") && !estado.equals("Cancelled")) {
                throw new InvalidDataException(
                    "Contrato",
                    "El estado debe ser uno de: Draft, Signed, Cancelled"
                );
            }
        }

        contratoExistente.setRutaArchivo(contratoActualizado.getRutaArchivo());
        contratoExistente.setFechaFirma(contratoActualizado.getFechaFirma());
        contratoExistente.setEstado(contratoActualizado.getEstado());
        contratoExistente.setCondicionEspecial(contratoActualizado.getCondicionEspecial());

        try {
            return contratoRepository.save(contratoExistente);
        } catch (Exception e) {
            throw new UpdateEntityException(
                "Contrato",
                "Error al actualizar Contrato: " + e.getMessage()
            );
        }
    }
} 