package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.ClienteProspecto;
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
    private final ClienteProspectoService clienteProspectoService;

    // Estados de solicitud
    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_EN_REVISION = "EN_REVISION";
    private static final String ESTADO_APROBADA = "APROBADA";
    private static final String ESTADO_RECHAZADA = "RECHAZADA";
    private static final String ESTADO_DESEMBOLSADA = "DESEMBOLSADA";

    // Límites de negocio
    private static final BigDecimal MONTO_MINIMO = new BigDecimal("5000");
    private static final BigDecimal MONTO_MAXIMO = new BigDecimal("150000");
    private static final Integer PLAZO_MINIMO = 12;
    private static final Integer PLAZO_MAXIMO = 84;
    private static final BigDecimal TASA_MINIMA = new BigDecimal("8.50");
    private static final BigDecimal TASA_MAXIMA = new BigDecimal("25.00");

    /**
     * Crear nueva solicitud de crédito
     */
    public SolicitudCredito crear(SolicitudCredito solicitud) {
        try {
            validarSolicitudCredito(solicitud);
            validarCapacidadCliente(solicitud.getIdClienteProspecto(), solicitud.getMontoSolicitado());
            validarSolicitudPendiente(solicitud.getIdClienteProspecto());
            
            solicitud.setEstado(ESTADO_PENDIENTE);
            solicitud.setFechaSolicitud(LocalDateTime.now());
            
            // Calcular cuota mensual
            BigDecimal cuotaMensual = calcularCuotaMensual(
                solicitud.getMontoSolicitado(),
                solicitud.getTasaInteres(),
                solicitud.getPlazoMeses()
            );
            solicitud.setCuotaMensual(cuotaMensual);
            
            return solicitudCreditoRepository.save(solicitud);
        } catch (Exception e) {
            throw new BusinessException("Error al crear solicitud de crédito: " + e.getMessage(), "CREAR_SOLICITUD");
        }
    }

    /**
     * Actualizar solicitud existente (solo si está pendiente)
     */
    public SolicitudCredito actualizar(Integer id, SolicitudCredito solicitudActualizada) {
        try {
            SolicitudCredito solicitudExistente = buscarPorId(id);
            
            if (!ESTADO_PENDIENTE.equals(solicitudExistente.getEstado())) {
                throw new BusinessException("Solo se pueden actualizar solicitudes pendientes", "ACTUALIZAR_SOLICITUD");
            }
            
            validarSolicitudCredito(solicitudActualizada);
            
            solicitudExistente.setMontoSolicitado(solicitudActualizada.getMontoSolicitado());
            solicitudExistente.setPlazoMeses(solicitudActualizada.getPlazoMeses());
            solicitudExistente.setTasaInteres(solicitudActualizada.getTasaInteres());
            
            // Recalcular cuota mensual
            BigDecimal cuotaMensual = calcularCuotaMensual(
                solicitudExistente.getMontoSolicitado(),
                solicitudExistente.getTasaInteres(),
                solicitudExistente.getPlazoMeses()
            );
            solicitudExistente.setCuotaMensual(cuotaMensual);
            
            return solicitudCreditoRepository.save(solicitudExistente);
        } catch (Exception e) {
            throw new BusinessException("Error al actualizar solicitud: " + e.getMessage(), "ACTUALIZAR_SOLICITUD");
        }
    }

    /**
     * Buscar solicitud por ID
     */
    @Transactional(readOnly = true)
    public SolicitudCredito buscarPorId(Integer id) {
        return solicitudCreditoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "SolicitudCredito"));
    }

    /**
     * Listar solicitudes por cliente
     */
    @Transactional(readOnly = true)
    public List<SolicitudCredito> listarPorCliente(Integer idCliente) {
        return solicitudCreditoRepository.findByIdClienteProspecto(idCliente);
    }

    /**
     * Listar solicitudes por estado
     */
    @Transactional(readOnly = true)
    public List<SolicitudCredito> listarPorEstado(String estado) {
        return solicitudCreditoRepository.findByEstado(estado);
    }

    /**
     * Listar solicitudes pendientes
     */
    @Transactional(readOnly = true)
    public List<SolicitudCredito> listarPendientes() {
        return solicitudCreditoRepository.findByEstado(ESTADO_PENDIENTE);
    }

    /**
     * Listar solicitudes por vendedor
     */
    @Transactional(readOnly = true)
    public List<SolicitudCredito> listarPorVendedor(Integer idVendedor) {
        return solicitudCreditoRepository.findByIdVendedor(idVendedor);
    }

    /**
     * Listar solicitudes en un rango de fechas
     */
    @Transactional(readOnly = true)
    public List<SolicitudCredito> listarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return solicitudCreditoRepository.findByFechaSolicitudBetween(fechaInicio, fechaFin);
    }

    /**
     * Enviar solicitud a revisión
     */
    public SolicitudCredito enviarARevision(Integer id) {
        SolicitudCredito solicitud = buscarPorId(id);
        
        if (!ESTADO_PENDIENTE.equals(solicitud.getEstado())) {
            throw new BusinessException("Solo se pueden enviar a revisión solicitudes pendientes", "ENVIAR_REVISION");
        }
        
        solicitud.setEstado(ESTADO_EN_REVISION);
        return solicitudCreditoRepository.save(solicitud);
    }

    /**
     * Aprobar solicitud de crédito
     */
    public SolicitudCredito aprobar(Integer id, String observaciones) {
        SolicitudCredito solicitud = buscarPorId(id);
        
        if (!ESTADO_EN_REVISION.equals(solicitud.getEstado())) {
            throw new BusinessException("Solo se pueden aprobar solicitudes en revisión", "APROBAR_SOLICITUD");
        }
        
        solicitud.setEstado(ESTADO_APROBADA);
        solicitud.setFechaAprobacion(LocalDateTime.now());
        solicitud.setMotivo(observaciones);
        
        return solicitudCreditoRepository.save(solicitud);
    }

    /**
     * Rechazar solicitud de crédito
     */
    public SolicitudCredito rechazar(Integer id, String motivo) {
        SolicitudCredito solicitud = buscarPorId(id);
        
        if (ESTADO_APROBADA.equals(solicitud.getEstado()) || ESTADO_DESEMBOLSADA.equals(solicitud.getEstado())) {
            throw new BusinessException("No se puede rechazar una solicitud aprobada o desembolsada", "RECHAZAR_SOLICITUD");
        }
        
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new ValidationException("motivo", "requerido para rechazar solicitud");
        }
        
        solicitud.setEstado(ESTADO_RECHAZADA);
        solicitud.setMotivo(motivo);
        
        return solicitudCreditoRepository.save(solicitud);
    }

    /**
     * Marcar como desembolsada
     */
    public SolicitudCredito marcarDesembolsada(Integer id) {
        SolicitudCredito solicitud = buscarPorId(id);
        
        if (!ESTADO_APROBADA.equals(solicitud.getEstado())) {
            throw new BusinessException("Solo se pueden desembolsar solicitudes aprobadas", "DESEMBOLSAR");
        }
        
        solicitud.setEstado(ESTADO_DESEMBOLSADA);
        return solicitudCreditoRepository.save(solicitud);
    }

    /**
     * Calcular cuota mensual usando método francés
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularCuotaMensual(BigDecimal monto, BigDecimal tasaAnual, Integer plazoMeses) {
        // Convertir tasa anual a mensual
        BigDecimal tasaMensual = tasaAnual.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)
                                        .divide(new BigDecimal("12"), 6, RoundingMode.HALF_UP);
        
        // Fórmula: M = P * [r(1+r)^n] / [(1+r)^n - 1]
        BigDecimal unoPlusTasa = BigDecimal.ONE.add(tasaMensual);
        BigDecimal potencia = unoPlusTasa.pow(plazoMeses);
        
        BigDecimal numerador = monto.multiply(tasaMensual).multiply(potencia);
        BigDecimal denominador = potencia.subtract(BigDecimal.ONE);
        
        return numerador.divide(denominador, 2, RoundingMode.HALF_UP);
    }

    /**
     * Simular crédito (sin guardar)
     */
    @Transactional(readOnly = true)
    public SolicitudCredito simularCredito(BigDecimal monto, BigDecimal tasa, Integer plazo) {
        SolicitudCredito simulacion = new SolicitudCredito();
        simulacion.setMontoSolicitado(monto);
        simulacion.setTasaInteres(tasa);
        simulacion.setPlazoMeses(plazo);
        
        BigDecimal cuotaMensual = calcularCuotaMensual(monto, tasa, plazo);
        simulacion.setCuotaMensual(cuotaMensual);
        
        return simulacion;
    }

    /**
     * Obtener estadísticas de solicitudes
     */
    @Transactional(readOnly = true)
    public long contarPorEstado(String estado) {
        return solicitudCreditoRepository.countByEstado(estado);
    }

    // Métodos privados de validación
    private void validarSolicitudCredito(SolicitudCredito solicitud) {
        if (solicitud.getIdClienteProspecto() == null) {
            throw new ValidationException("idClienteProspecto", "requerido");
        }
        
        if (solicitud.getMontoSolicitado() == null) {
            throw new ValidationException("montoSolicitado", "requerido");
        }
        
        if (solicitud.getMontoSolicitado().compareTo(MONTO_MINIMO) < 0) {
            throw new ValidationException("montoSolicitado", "debe ser mayor a " + MONTO_MINIMO);
        }
        
        if (solicitud.getMontoSolicitado().compareTo(MONTO_MAXIMO) > 0) {
            throw new ValidationException("montoSolicitado", "debe ser menor a " + MONTO_MAXIMO);
        }
        
        if (solicitud.getPlazoMeses() == null) {
            throw new ValidationException("plazoMeses", "requerido");
        }
        
        if (solicitud.getPlazoMeses() < PLAZO_MINIMO) {
            throw new ValidationException("plazoMeses", "debe ser mayor a " + PLAZO_MINIMO);
        }
        
        if (solicitud.getPlazoMeses() > PLAZO_MAXIMO) {
            throw new ValidationException("plazoMeses", "debe ser menor a " + PLAZO_MAXIMO);
        }
        
        if (solicitud.getTasaInteres() == null) {
            throw new ValidationException("tasaInteres", "requerida");
        }
        
        if (solicitud.getTasaInteres().compareTo(TASA_MINIMA) < 0) {
            throw new ValidationException("tasaInteres", "debe ser mayor a " + TASA_MINIMA + "%");
        }
        
        if (solicitud.getTasaInteres().compareTo(TASA_MAXIMA) > 0) {
            throw new ValidationException("tasaInteres", "debe ser menor a " + TASA_MAXIMA + "%");
        }
    }

    private void validarCapacidadCliente(Integer idCliente, BigDecimal montoSolicitado) {
        if (!clienteProspectoService.puedesolicitarCredito(idCliente)) {
            throw new BusinessException("Cliente no cumple con los requisitos para solicitar crédito", "VALIDAR_CAPACIDAD");
        }
        
        BigDecimal capacidadPago = clienteProspectoService.calcularCapacidadPago(idCliente);
        BigDecimal cuotaEstimada = calcularCuotaMensual(montoSolicitado, TASA_MAXIMA, PLAZO_MINIMO);
        
        if (cuotaEstimada.compareTo(capacidadPago) > 0) {
            throw new BusinessException("El monto solicitado excede la capacidad de pago del cliente", "VALIDAR_CAPACIDAD");
        }
    }

    private void validarSolicitudPendiente(Integer idCliente) {
        if (solicitudCreditoRepository.existsByIdClienteProspectoAndEstado(idCliente, ESTADO_PENDIENTE) ||
            solicitudCreditoRepository.existsByIdClienteProspectoAndEstado(idCliente, ESTADO_EN_REVISION)) {
            throw new BusinessException("El cliente ya tiene una solicitud pendiente o en revisión", "VALIDAR_SOLICITUD_PENDIENTE");
        }
    }
} 