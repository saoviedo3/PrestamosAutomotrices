package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.repository.PagareRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagareService {
    private final PagareRepository pagareRepository;
    private final SolicitudCreditoService solicitudService;

    public PagareService(PagareRepository pagareRepository, 
                        SolicitudCreditoService solicitudService) {
        this.pagareRepository = pagareRepository;
        this.solicitudService = solicitudService;
    }

    @Transactional(readOnly = true)
    public List<Pagare> getAll() {
        return pagareRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pagare getById(Long id) {
        return pagareRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("Pagare", "No existe un pagaré con el id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pagare> getBySolicitudId(Long idSolicitud) {
        solicitudService.getById(idSolicitud);
        return pagareRepository.findByIdSolicitudOrderByNumeroCuotaAsc(idSolicitud);
    }

    @Transactional
    public Pagare create(Pagare pagare) {
        solicitudService.getById(pagare.getIdSolicitud());

        if (pagareRepository.existsByIdSolicitudAndNumeroCuota(
                pagare.getIdSolicitud(), 
                pagare.getNumeroCuota())) {
            throw new AlreadyExistsException(
                "Pagare",
                "Ya existe un pagaré para la solicitud " + pagare.getIdSolicitud() + 
                " y cuota " + pagare.getNumeroCuota()
            );
        }

        if (pagare.getNumeroCuota() <= 0) {
            throw new InvalidDataException(
                "Pagare",
                "El número de cuota debe ser mayor a cero"
            );
        }

        if (pagare.getRutaArchivo() == null || pagare.getRutaArchivo().isBlank()) {
            throw new InvalidDataException(
                "Pagare",
                "La ruta del archivo no puede estar vacía"
            );
        }

        if (pagare.getFechaGenerado() == null) {
            pagare.setFechaGenerado(LocalDateTime.now());
        }

        try {
            return pagareRepository.save(pagare);
        } catch (Exception e) {
            throw new CreateEntityException(
                "Pagare",
                "Error al crear Pagare: " + e.getMessage()
            );
        }
    }

    @Transactional
    public List<Pagare> createAll(List<Pagare> pagares) {
        if (pagares == null || pagares.isEmpty()) {
            throw new InvalidDataException(
                "Pagare",
                "La lista de pagarés no puede estar vacía"
            );
        }

            Long idSolicitud = pagares.get(0).getIdSolicitud();
            solicitudService.getById(idSolicitud);

            for (Pagare pagare : pagares) {
                if (!pagare.getIdSolicitud().equals(idSolicitud)) {
                    throw new InvalidDataException(
                        "Pagare",
                        "Todos los pagarés deben pertenecer a la misma solicitud"
                    );
                }
                if (pagareRepository.existsByIdSolicitudAndNumeroCuota(
                        pagare.getIdSolicitud(), 
                        pagare.getNumeroCuota())) {
                    throw new AlreadyExistsException(
                        "Pagare",
                        "Ya existe un pagaré para la solicitud " + pagare.getIdSolicitud() + 
                        " y cuota " + pagare.getNumeroCuota()
                    );
                }
            if (pagare.getNumeroCuota() <= 0) {
                throw new InvalidDataException(
                    "Pagare",
                    "El número de cuota debe ser mayor a cero"
                );
            }
            if (pagare.getRutaArchivo() == null || pagare.getRutaArchivo().isBlank()) {
                throw new InvalidDataException(
                    "Pagare",
                    "La ruta del archivo no puede estar vacía"
                );
            }
        }

        LocalDateTime now = LocalDateTime.now();
        pagares.forEach(p -> {
            if (p.getFechaGenerado() == null) {
                p.setFechaGenerado(now);
            }
        });

        try {
            return pagareRepository.saveAll(pagares);
        } catch (Exception e) {
            throw new CreateEntityException(
                "Pagare",
                "Error al crear Pagares: " + e.getMessage()
            );
        }
    }
} 