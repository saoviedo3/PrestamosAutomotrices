package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.PagareNotFoundException;
import com.banquito.sistema.originacion.exception.SolicitudCreditoNotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.repository.PagareRepository;
import com.banquito.sistema.originacion.repository.SolicitudCreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagareService {

    private final PagareRepository pagareRepository;
    private final SolicitudCreditoRepository solicitudCreditoRepository;

    private static final String ESTADO_SOLICITUD_APROBADA = "APROBADA";

    /**
     * Crear un nuevo pagaré
     */
    public Pagare createPagare(Pagare pagare) {
        try {
            validarPagare(pagare);
            validarSolicitudCredito(pagare.getIdSolicitud());
            validarReglasPagare(pagare);
            
            pagare.setFechaGenerado(LocalDateTime.now());
            return pagareRepository.save(pagare);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new BusinessException("Error al crear pagaré: " + e.getMessage(), "CREAR_PAGARE");
        }
    }

    /**
     * Buscar pagaré por ID
     */
    @Transactional(readOnly = true)
    public Pagare getPagareById(Integer id) {
        return pagareRepository.findById(id)
                .orElseThrow(() -> new PagareNotFoundException(id.toString(), "ID"));
    }

    /**
     * Listar pagarés por solicitud de crédito
     */
    @Transactional(readOnly = true)
    public List<Pagare> listarPorSolicitud(Integer idSolicitud) {
        validarSolicitudCredito(idSolicitud);
        return pagareRepository.findBySolicitudCredito_IdSolicitudOrderByNumeroCuotaAsc(idSolicitud);
    }

    /**
     * Buscar pagaré por solicitud y número de cuota
     */
    @Transactional(readOnly = true)
    public Pagare buscarPorSolicitudYCuota(Integer idSolicitud, Integer numeroCuota) {
        return pagareRepository.findBySolicitudCredito_IdSolicitudAndNumeroCuota(idSolicitud, numeroCuota)
                .orElseThrow(() -> new PagareNotFoundException(
                    "Solicitud: " + idSolicitud + ", Cuota: " + numeroCuota, 
                    "solicitud y número de cuota"));
    }

    /**
     * Contar pagarés por solicitud
     */
    @Transactional(readOnly = true)
    public long contarPorSolicitud(Integer idSolicitud) {
        return pagareRepository.countBySolicitudCredito_IdSolicitud(idSolicitud);
    }

    /**
     * Verificar si existe pagaré para una solicitud y cuota específica
     */
    @Transactional(readOnly = true)
    public boolean existePagare(Integer idSolicitud, Integer numeroCuota) {
        return pagareRepository.existsBySolicitudCredito_IdSolicitudAndNumeroCuota(idSolicitud, numeroCuota);
    }

    /**
     * Listar pagarés por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Pagare> listarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pagareRepository.findByFechaGeneradoBetween(fechaInicio, fechaFin);
    }

    // Métodos privados de validación
    private void validarPagare(Pagare pagare) {
        if (pagare.getIdSolicitud() == null) {
            throw new ValidationException("idSolicitud", "requerido");
        }
        
        if (pagare.getNumeroCuota() == null || pagare.getNumeroCuota() <= 0) {
            throw new ValidationException("numeroCuota", "debe ser mayor a cero");
        }
        
        if (pagare.getRutaArchivo() == null || pagare.getRutaArchivo().trim().isEmpty()) {
            throw new ValidationException("rutaArchivo", "requerida");
        }
    }

    private void validarSolicitudCredito(Integer idSolicitud) {
        SolicitudCredito solicitud = solicitudCreditoRepository.findById(idSolicitud)
                .orElseThrow(() -> new SolicitudCreditoNotFoundException(idSolicitud.toString(), "ID"));
        
        if (!ESTADO_SOLICITUD_APROBADA.equals(solicitud.getEstado())) {
            throw new BusinessException("La solicitud de crédito debe estar aprobada para generar pagarés", "SOLICITUD_NO_APROBADA");
        }
    }

    private void validarReglasPagare(Pagare pagare) {
        // Verificar que no exista ya un pagaré para la misma solicitud y cuota
        if (pagareRepository.existsBySolicitudCredito_IdSolicitudAndNumeroCuota(
                pagare.getIdSolicitud(), pagare.getNumeroCuota())) {
            throw new BusinessException(
                "Ya existe un pagaré para la solicitud " + pagare.getIdSolicitud() + 
                " y cuota " + pagare.getNumeroCuota(), "PAGARE_DUPLICADO");
        }
        
        // Verificar que la ruta del archivo no esté duplicada
        if (pagareRepository.existsByRutaArchivo(pagare.getRutaArchivo())) {
            throw new BusinessException("La ruta del archivo ya está en uso", "RUTA_ARCHIVO_DUPLICADA");
        }
    }
} 