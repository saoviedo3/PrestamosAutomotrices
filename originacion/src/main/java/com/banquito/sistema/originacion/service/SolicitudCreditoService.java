package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.repository.SolicitudCreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudCreditoService {

    private final SolicitudCreditoRepository solicitudCreditoRepository;

    private static final String ESTADO_BORRADOR = "Borrador";
    private static final String ESTADO_EN_REVISION = "EnRevision";
    private static final String ESTADO_APROBADA = "Aprobada";
    private static final String ESTADO_RECHAZADA = "Rechazada";
    private static final String ESTADO_CANCELADA = "Cancelada";
    private static final String ESTADO_DESEMBOLSADA = "Desembolsada";

    public SolicitudCredito crear(SolicitudCredito solicitudCredito) {
        try {
            validarSolicitudCredito(solicitudCredito);
            validarNumeroSolicitudUnico(solicitudCredito.getNumeroSolicitud());
            validarVehiculoUnico(solicitudCredito.getIdVehiculo());
            
            solicitudCredito.setEstado(ESTADO_BORRADOR);
            solicitudCredito.setFechaSolicitud(LocalDateTime.now());
            return solicitudCreditoRepository.save(solicitudCredito);
        } catch (Exception e) {
            throw new BusinessException("Error al crear solicitud de crédito: " + e.getMessage(), "CREAR_SOLICITUD_CREDITO");
        }
    }

    public SolicitudCredito actualizar(Integer id, SolicitudCredito solicitudCredito) {
        try {
            SolicitudCredito solicitudExistente = buscarPorId(id);
            
            validarSolicitudCredito(solicitudCredito);
            
            if (!solicitudExistente.getNumeroSolicitud().equals(solicitudCredito.getNumeroSolicitud())) {
                validarNumeroSolicitudUnico(solicitudCredito.getNumeroSolicitud());
            }
            
            if (!solicitudExistente.getIdVehiculo().equals(solicitudCredito.getIdVehiculo())) {
                validarVehiculoUnico(solicitudCredito.getIdVehiculo());
            }
            
            solicitudCredito.setIdSolicitud(id);
            solicitudCredito.setEstado(solicitudExistente.getEstado());
            
            return solicitudCreditoRepository.save(solicitudCredito);
        } catch (Exception e) {
            throw new BusinessException("Error al actualizar solicitud de crédito: " + e.getMessage(), "ACTUALIZAR_SOLICITUD_CREDITO");
        }
    }

    @Transactional(readOnly = true)
    public SolicitudCredito buscarPorId(Integer id) {
        return solicitudCreditoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Solicitud de crédito no encontrada con ID: " + id, "BUSCAR_SOLICITUD_CREDITO"));
    }

    @Transactional(readOnly = true)
    public SolicitudCredito buscarPorNumeroSolicitud(String numeroSolicitud) {
        return solicitudCreditoRepository.findByNumeroSolicitud(numeroSolicitud)
                .orElseThrow(() -> new BusinessException("Solicitud de crédito no encontrada con número: " + numeroSolicitud, "BUSCAR_SOLICITUD_CREDITO"));
    }

    @Transactional(readOnly = true)
    public List<SolicitudCredito> listarPorEstado(String estado) {
        return solicitudCreditoRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<SolicitudCredito> listarPorClienteProspecto(Integer idClienteProspecto) {
        return solicitudCreditoRepository.findByIdClienteProspecto(idClienteProspecto);
    }

    @Transactional(readOnly = true)
    public List<SolicitudCredito> listarPorVendedor(Integer idVendedor) {
        return solicitudCreditoRepository.findByIdVendedor(idVendedor);
    }

    public SolicitudCredito enviarARevision(Integer id) {
        SolicitudCredito solicitud = buscarPorId(id);
        validarEstadoParaCambio(solicitud, ESTADO_BORRADOR);
        solicitud.setEstado(ESTADO_EN_REVISION);
        return solicitudCreditoRepository.save(solicitud);
    }

    public SolicitudCredito aprobar(Integer id, String observaciones) {
        SolicitudCredito solicitud = buscarPorId(id);
        validarEstadoParaCambio(solicitud, ESTADO_EN_REVISION);
        solicitud.setEstado(ESTADO_APROBADA);
        return solicitudCreditoRepository.save(solicitud);
    }

    public SolicitudCredito rechazar(Integer id, String motivo) {
        SolicitudCredito solicitud = buscarPorId(id);
        validarEstadoParaCambio(solicitud, ESTADO_EN_REVISION);
        solicitud.setEstado(ESTADO_RECHAZADA);
        return solicitudCreditoRepository.save(solicitud);
    }

    public SolicitudCredito cancelar(Integer id) {
        SolicitudCredito solicitud = buscarPorId(id);
        validarEstadoParaCambio(solicitud, ESTADO_BORRADOR);
        solicitud.setEstado(ESTADO_CANCELADA);
        return solicitudCreditoRepository.save(solicitud);
    }

    public SolicitudCredito marcarDesembolsada(Integer id) {
        SolicitudCredito solicitud = buscarPorId(id);
        if (!ESTADO_APROBADA.equals(solicitud.getEstado())) {
            throw new BusinessException("Solo se pueden desembolsar solicitudes aprobadas", "MARCAR_DESEMBOLSADA");
        }
        solicitud.setEstado(ESTADO_DESEMBOLSADA);
        return solicitudCreditoRepository.save(solicitud);
    }

    public BigDecimal calcularCuotaMensual(BigDecimal monto, BigDecimal tasa, Integer plazo) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0 || tasa.compareTo(BigDecimal.ZERO) <= 0 || plazo <= 0) {
            throw new ValidationException("Parámetros inválidos para el cálculo de cuota", "CALCULAR_CUOTA");
        }

        BigDecimal tasaMensual = tasa.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)
                .divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP);
        
        BigDecimal factor = BigDecimal.ONE.add(tasaMensual).pow(plazo);
        BigDecimal numerador = monto.multiply(tasaMensual).multiply(factor);
        BigDecimal denominador = factor.subtract(BigDecimal.ONE);
        
        return numerador.divide(denominador, 2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public List<SolicitudCredito> findAll() {
        return solicitudCreditoRepository.findAll();
    }

    @Transactional
    public void delete(Integer id) {
        SolicitudCredito solicitud = buscarPorId(id);
        solicitudCreditoRepository.delete(solicitud);
    }

    private void validarSolicitudCredito(SolicitudCredito solicitud) {
        if (solicitud.getMontoSolicitado() == null || solicitud.getMontoSolicitado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("montoSolicitado", "debe ser mayor a cero");
        }
        
        if (solicitud.getPlazoMeses() == null || solicitud.getPlazoMeses().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("plazoMeses", "debe ser mayor a cero");
        }
        
        if (solicitud.getEntrada() == null || solicitud.getEntrada().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("entrada", "no puede ser negativa");
        }
        
        if (solicitud.getTasaAnual() == null || solicitud.getTasaAnual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("tasaAnual", "debe ser mayor a cero");
        }
    }

    private void validarNumeroSolicitudUnico(String numeroSolicitud) {
        if (solicitudCreditoRepository.existsByNumeroSolicitud(numeroSolicitud)) {
            throw new ValidationException("numeroSolicitud", "ya existe en el sistema");
        }
    }

    private void validarVehiculoUnico(Integer idVehiculo) {
        if (solicitudCreditoRepository.existsByIdVehiculo(idVehiculo)) {
            throw new ValidationException("idVehiculo", "ya tiene una solicitud asociada");
        }
    }

    private void validarEstadoParaCambio(SolicitudCredito solicitud, String estadoEsperado) {
        if (!estadoEsperado.equals(solicitud.getEstado())) {
            throw new BusinessException("La solicitud debe estar en estado " + estadoEsperado, "VALIDAR_ESTADO");
        }
    }
} 