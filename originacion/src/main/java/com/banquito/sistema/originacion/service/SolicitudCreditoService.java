package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.repository.SolicitudCreditoRepository;
import com.banquito.sistema.originacion.service.exception.CreditoException;
import com.banquito.sistema.originacion.exception.SolicitudCreditoNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudCreditoService {

    private final SolicitudCreditoRepository solicitudCreditoRepository;

    // Constantes para validación de Score
    private static final BigDecimal SCORE_RECHAZO_AUTOMATICO = new BigDecimal("500.0");
    private static final BigDecimal SCORE_CLIENTE_C = new BigDecimal("600.0");
    private static final BigDecimal SCORE_CLIENTE_B = new BigDecimal("750.0");

    public SolicitudCreditoService(SolicitudCreditoRepository solicitudCreditoRepository) {
        this.solicitudCreditoRepository = solicitudCreditoRepository;
    }

    public List<SolicitudCredito> findAll() {
        return this.solicitudCreditoRepository.findAll();
    }

    public List<SolicitudCredito> findByEstado(String estado) {
        return this.solicitudCreditoRepository.findByEstado(estado);
    }

    // public List<SolicitudCredito> findByIdClienteProspecto(Integer idClienteProspecto) {
    //     return this.solicitudCreditoRepository.findByIdClienteProspecto(idClienteProspecto);
    // }

    // public List<SolicitudCredito> findByIdVendedor(Integer idVendedor) {
    //     return this.solicitudCreditoRepository.findByIdVendedor(idVendedor);
    // }

    public SolicitudCredito findById(Long id) {
        Optional<SolicitudCredito> solicitudOpt = this.solicitudCreditoRepository.findById(id);
        if (solicitudOpt.isPresent()) {
            return solicitudOpt.get();
        } else {
            throw new CreditoException("No existe la solicitud de crédito con ID: " + id);
        }
    }

    public SolicitudCredito findByNumeroSolicitud(String numeroSolicitud) {
        Optional<SolicitudCredito> solicitudOpt = this.solicitudCreditoRepository.findByNumeroSolicitud(numeroSolicitud);
        if (solicitudOpt.isPresent()) {
            return solicitudOpt.get();
        } else {
            throw new CreditoException("No existe la solicitud de crédito con número: " + numeroSolicitud);
        }
    }

    @Transactional
    public SolicitudCredito save(SolicitudCredito solicitud) {
        // Verificar si ya existe una solicitud con el mismo número
        if (this.solicitudCreditoRepository.findByNumeroSolicitud(solicitud.getNumeroSolicitud()).isPresent()) {
            throw new CreditoException("Ya existe una solicitud con el número: " + solicitud.getNumeroSolicitud());
        }
        
        // Verificar si el vehículo ya está asignado a otra solicitud
        if (this.solicitudCreditoRepository.existsByIdVehiculo(solicitud.getIdVehiculo())) {
            throw new CreditoException("El vehículo con ID " + solicitud.getIdVehiculo() + " ya está asignado a otra solicitud");
        }
        
        // Inicializar valores por defecto si no están establecidos
        if (solicitud.getScoreInterno() == null) {
            solicitud.setScoreInterno(BigDecimal.ZERO);
        }
        
        if (solicitud.getScoreExterno() == null) {
            solicitud.setScoreExterno(BigDecimal.ZERO);
        }
        
        if (solicitud.getRelacionCuotaIngreso() == null) {
            solicitud.setRelacionCuotaIngreso(BigDecimal.ZERO);
        }
        
        if (solicitud.getEstado() == null) {
            solicitud.setEstado("Borrador");
        }
        
        // Recalcular los valores financieros
        calcularValoresFinancieros(solicitud);
        
        return this.solicitudCreditoRepository.save(solicitud);
    }

    @Transactional
    public SolicitudCredito update(SolicitudCredito solicitud) {
        Optional<SolicitudCredito> solicitudOpt = this.solicitudCreditoRepository.findById(solicitud.getId());
        if (!solicitudOpt.isPresent()) {
            throw new CreditoException("No existe la solicitud de crédito con ID: " + solicitud.getId());
        }
        
        SolicitudCredito solicitudExistente = solicitudOpt.get();
        
        // Verificar que la solicitud esté en estado Borrador
        if (!"Borrador".equals(solicitudExistente.getEstado())) {
            throw new CreditoException("No se puede actualizar una solicitud que no está en estado Borrador");
        }
        
        // Verificar si el número de solicitud está siendo cambiado y ya existe
        if (!solicitudExistente.getNumeroSolicitud().equals(solicitud.getNumeroSolicitud()) &&
            this.solicitudCreditoRepository.findByNumeroSolicitud(solicitud.getNumeroSolicitud()).isPresent()) {
            throw new CreditoException("Ya existe una solicitud con el número: " + solicitud.getNumeroSolicitud());
        }
        
        // Verificar si el vehículo está cambiando y ya está asignado
        if (!solicitudExistente.getIdVehiculo().equals(solicitud.getIdVehiculo()) &&
            this.solicitudCreditoRepository.existsByIdVehiculo(solicitud.getIdVehiculo())) {
            throw new CreditoException("El vehículo con ID " + solicitud.getIdVehiculo() + " ya está asignado a otra solicitud");
        }
        
        // Recalcular los valores financieros
        calcularValoresFinancieros(solicitud);
        
        return this.solicitudCreditoRepository.save(solicitud);
    }

    /**
     * Realiza una evaluación inicial automática de la solicitud
     * y determina si debe ser rechazada automáticamente.
     * @param solicitud La solicitud a evaluar
     * @return La solicitud con el estado actualizado
     */
    @Transactional
    public SolicitudCredito evaluarSolicitud(SolicitudCredito solicitud) {
        // Verificar que la solicitud esté en estado Borrador
        if (!"Borrador".equals(solicitud.getEstado())) {
            throw new CreditoException("Solo se pueden evaluar solicitudes en estado Borrador");
        }
        
        // Simulamos la consulta al buró de crédito y establecemos el score externo
        // En un caso real, aquí llamaríamos a un servicio externo
        
        // Aplicar reglas automáticas de rechazo
        if (solicitud.getScoreExterno().compareTo(SCORE_RECHAZO_AUTOMATICO) < 0) {
            // Score menor a 500, rechazo automático
            solicitud.setEstado("Rechazada");
            return this.solicitudCreditoRepository.save(solicitud);
        }
        
        // Verificación de relación cuota/ingreso no debe superar el 30%
        if (solicitud.getRelacionCuotaIngreso().compareTo(new BigDecimal("0.3")) > 0) {
            // Relación cuota/ingreso mayor al 30%, enviar a revisión manual
            solicitud.setEstado("EnRevision");
        } else {
            // Evaluación automática pasada, determinar si necesita revisión manual
            if (solicitud.getScoreExterno().compareTo(SCORE_CLIENTE_B) >= 0) {
                // Cliente A - Aprobación automática
                solicitud.setEstado("Aprobada");
            } else {
                // Cliente B o C - Enviar a revisión manual
                solicitud.setEstado("EnRevision");
            }
        }
        
        return this.solicitudCreditoRepository.save(solicitud);
    }

    /**
     * Método para que un analista cambie el estado de una solicitud
     * @param id ID de la solicitud
     * @param nuevoEstado Nuevo estado de la solicitud
     * @param motivoCambio Motivo del cambio de estado (será registrado en ObservacionAnalista)
     * @return La solicitud con el estado actualizado
     */
    @Transactional
    public SolicitudCredito cambiarEstado(Long id, String nuevoEstado) {
        Optional<SolicitudCredito> solicitudOpt = this.solicitudCreditoRepository.findById(id);
        if (!solicitudOpt.isPresent()) {
            throw new CreditoException("No existe la solicitud de crédito con ID: " + id);
        }
        
        SolicitudCredito solicitud = solicitudOpt.get();
        
        // Validar transiciones de estado permitidas
        validarTransicionEstado(solicitud.getEstado(), nuevoEstado);
        
        // Verificar si es una decisión de aprobación/rechazo por analista
        if (("Aprobada".equals(nuevoEstado) || "Rechazada".equals(nuevoEstado)) 
                && "EnRevision".equals(solicitud.getEstado())) {
            // Aquí se registraría la decisión del analista
            // Pero eso se hará a través de ObservacionAnalista
        }
        
        solicitud.setEstado(nuevoEstado);
        return this.solicitudCreditoRepository.save(solicitud);
    }

    @Transactional
    public void delete(Long id) {
        Optional<SolicitudCredito> solicitudOpt = this.solicitudCreditoRepository.findById(id);
        if (!solicitudOpt.isPresent()) {
            throw new CreditoException("No existe la solicitud de crédito con ID: " + id);
        }
        
        SolicitudCredito solicitud = solicitudOpt.get();
        
        // Solo se pueden eliminar solicitudes en borrador
        if (!"Borrador".equals(solicitud.getEstado())) {
            throw new CreditoException("Solo se pueden eliminar solicitudes en estado Borrador");
        }
        
        this.solicitudCreditoRepository.delete(solicitud);
    }
    
    private void calcularValoresFinancieros(SolicitudCredito solicitud) {
        // En un caso real, aquí se aplicarían reglas de negocio para calcular
        // la tasa anual basada en perfil del cliente, plazo, monto, etc.
        BigDecimal tasaAnual = new BigDecimal("12.5"); // Tasa ejemplo
        
        // Para clientes de mayor riesgo, aplicar una tasa más alta
        if (solicitud.getScoreExterno() != null) {
            if (solicitud.getScoreExterno().compareTo(SCORE_CLIENTE_C) < 0) {
                // Cliente C - mayor tasa
                tasaAnual = new BigDecimal("18.5");
            } else if (solicitud.getScoreExterno().compareTo(SCORE_CLIENTE_B) < 0) {
                // Cliente B - tasa media
                tasaAnual = new BigDecimal("15.0");
            }
        }
        
        solicitud.setTasaAnual(tasaAnual);
        
        // Cálculo de cuota mensual usando fórmula de amortización
        BigDecimal tasaMensual = tasaAnual.divide(new BigDecimal("12"), 6, RoundingMode.HALF_UP)
                .divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
        
        BigDecimal factor = BigDecimal.ONE.add(tasaMensual).pow(solicitud.getPlazoMeses());
        BigDecimal cuotaMensual = solicitud.getMontoSolicitado().multiply(tasaMensual.multiply(factor))
                .divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        
        solicitud.setCuotaMensual(cuotaMensual);
        solicitud.setTotalPagar(cuotaMensual.multiply(new BigDecimal(solicitud.getPlazoMeses())));
    }
    
    private void validarTransicionEstado(String estadoActual, String nuevoEstado) {
        // Definir las transiciones permitidas
        boolean transicionValida = false;
        
        switch (estadoActual) {
            case "Borrador":
                transicionValida = "EnRevision".equals(nuevoEstado) || "Cancelada".equals(nuevoEstado);
                break;
            case "EnRevision":
                transicionValida = "Aprobada".equals(nuevoEstado) || "Rechazada".equals(nuevoEstado) 
                        || "Cancelada".equals(nuevoEstado);
                break;
            case "Aprobada":
            case "Rechazada":
            case "Cancelada":
                // Estados finales, no pueden cambiar
                transicionValida = false;
                break;
            default:
                transicionValida = false;
        }
        
        if (!transicionValida) {
            throw new CreditoException("No se permite cambiar el estado de " + estadoActual + " a " + nuevoEstado);
        }
    }
    
    /**
     * Clasifica al cliente según su score crediticio
     * @param scoreExterno Score externo del cliente
     * @return Clasificación del cliente (A, B, C)
     */
    public String clasificarCliente(BigDecimal scoreExterno) {
        if (scoreExterno.compareTo(SCORE_CLIENTE_B) >= 0) {
            return "A"; // Riesgo bajo
        } else if (scoreExterno.compareTo(SCORE_CLIENTE_C) >= 0) {
            return "B"; // Riesgo medio
        } else {
            return "C"; // Riesgo alto
        }
    }

    @Transactional(readOnly = true)
    public SolicitudCredito getById(Long id) {
        return solicitudCreditoRepository.findById(id)
                .orElseThrow(() -> new SolicitudCreditoNotFoundException(id));
    }
} 