package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.repository.SolicitudCreditoRepository;
import com.banquito.sistema.originacion.service.exception.CreditoException;
import com.banquito.sistema.originacion.exception.SolicitudCreditoNotFoundException;
import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.repository.ClienteProspectoRepository;
import com.banquito.sistema.originacion.model.Vehiculo;
import com.banquito.sistema.originacion.repository.VehiculoRepository;
import com.banquito.sistema.originacion.model.Vendedor;
import com.banquito.sistema.originacion.repository.VendedorRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudCreditoService {

    private final SolicitudCreditoRepository solicitudCreditoRepository;
    private final ClienteProspectoRepository clienteProspectoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final VendedorRepository vendedorRepository;

    // Constantes para validación de Score
    private static final BigDecimal SCORE_RECHAZO_AUTOMATICO = new BigDecimal("500.0");
    private static final BigDecimal SCORE_CLIENTE_C = new BigDecimal("600.0");
    private static final BigDecimal SCORE_CLIENTE_B = new BigDecimal("750.0");

    public SolicitudCreditoService(SolicitudCreditoRepository solicitudCreditoRepository, ClienteProspectoRepository clienteProspectoRepository, VehiculoRepository vehiculoRepository, VendedorRepository vendedorRepository) {
        this.solicitudCreditoRepository = solicitudCreditoRepository;
        this.clienteProspectoRepository = clienteProspectoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.vendedorRepository = vendedorRepository;
    }

    public List<SolicitudCredito> findAll() {
        return this.solicitudCreditoRepository.findAll();
    }

    public List<SolicitudCredito> findByEstado(String estado) {
        return this.solicitudCreditoRepository.findByEstado(estado);
    }

    // public List<SolicitudCredito> findByIdClienteProspecto(Long idClienteProspecto) {
    //     return this.solicitudCreditoRepository.findByIdClienteProspecto(idClienteProspecto);
    // }

    // public List<SolicitudCredito> findByIdVendedor(Long idVendedor) {
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
        if (solicitud.getIdVehiculo() != null) {
            if (this.solicitudCreditoRepository.existsByVehiculo_Id(solicitud.getIdVehiculo())) {
                throw new CreditoException("El vehículo con ID " + solicitud.getIdVehiculo() + " ya está asignado a otra solicitud");
            }
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
        // Asignar el objeto ClienteProspecto de forma obligatoria
        if (solicitud.getIdClienteProspecto() != null) {
            ClienteProspecto cp = clienteProspectoRepository.findById(solicitud.getIdClienteProspecto())
                .orElseThrow(() -> new CreditoException("No existe el ClienteProspecto con ID: " + solicitud.getIdClienteProspecto()));
            solicitud.setClienteProspecto(cp);
        }
        if (solicitud.getClienteProspecto() == null) {
            throw new CreditoException("El ClienteProspecto no puede ser nulo");
        }
        // Asignar el objeto Vehiculo de forma obligatoria
        if (solicitud.getIdVehiculo() != null) {
            Vehiculo v = vehiculoRepository.findById(solicitud.getIdVehiculo())
                .orElseThrow(() -> new CreditoException("No existe el Vehiculo con ID: " + solicitud.getIdVehiculo()));
            solicitud.setVehiculo(v);
        }
        if (solicitud.getVehiculo() == null) {
            throw new CreditoException("El Vehiculo no puede ser nulo");
        }
        // Asignar el objeto Vendedor de forma obligatoria
        if (solicitud.getIdVendedor() != null) {
            Vendedor v = vendedorRepository.findById(solicitud.getIdVendedor())
                .orElseThrow(() -> new CreditoException("No existe el Vendedor con ID: " + solicitud.getIdVendedor()));
            solicitud.setVendedor(v);
        }
        if (solicitud.getVendedor() == null) {
            throw new CreditoException("El Vendedor no puede ser nulo");
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
        if (solicitud.getIdVehiculo() != null && 
            (solicitudExistente.getVehiculo() == null || 
             !solicitudExistente.getVehiculo().getId().equals(solicitud.getIdVehiculo()))) {
            if (this.solicitudCreditoRepository.existsByVehiculo_Id(solicitud.getIdVehiculo())) {
                throw new CreditoException("El vehículo con ID " + solicitud.getIdVehiculo() + " ya está asignado a otra solicitud");
            }
        }
        // Asignar el objeto ClienteProspecto de forma obligatoria
        if (solicitud.getIdClienteProspecto() != null) {
            ClienteProspecto cp = clienteProspectoRepository.findById(solicitud.getIdClienteProspecto())
                .orElseThrow(() -> new CreditoException("No existe el ClienteProspecto con ID: " + solicitud.getIdClienteProspecto()));
            solicitud.setClienteProspecto(cp);
        }
        if (solicitud.getClienteProspecto() == null) {
            throw new CreditoException("El ClienteProspecto no puede ser nulo");
        }
        // Asignar el objeto Vehiculo de forma obligatoria
        if (solicitud.getIdVehiculo() != null) {
            Vehiculo v = vehiculoRepository.findById(solicitud.getIdVehiculo())
                .orElseThrow(() -> new CreditoException("No existe el Vehiculo con ID: " + solicitud.getIdVehiculo()));
            solicitud.setVehiculo(v);
        }
        if (solicitud.getVehiculo() == null) {
            throw new CreditoException("El Vehiculo no puede ser nulo");
        }
        // Asignar el objeto Vendedor de forma obligatoria
        if (solicitud.getIdVendedor() != null) {
            Vendedor v = vendedorRepository.findById(solicitud.getIdVendedor())
                .orElseThrow(() -> new CreditoException("No existe el Vendedor con ID: " + solicitud.getIdVendedor()));
            solicitud.setVendedor(v);
        }
        if (solicitud.getVendedor() == null) {
            throw new CreditoException("El Vendedor no puede ser nulo");
        }
        // Recalcular los valores financieros
        calcularValoresFinancieros(solicitud);
        // Asignar versión de la base si viene null
        if (solicitud.getVersion() == null) {
            solicitud.setVersion(solicitudExistente.getVersion());
        }
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
        
        // Calcular el score combinado (promedio entre interno y externo)
        BigDecimal scoreCombinado = solicitud.getScoreInterno().add(solicitud.getScoreExterno())
                                    .divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
        
        // Verificar si cumple el score mínimo o debe ser rechazada automáticamente
        if (scoreCombinado.compareTo(SCORE_RECHAZO_AUTOMATICO) < 0) {
            solicitud.setEstado("Rechazada");
            return this.solicitudCreditoRepository.save(solicitud);
        }
        
        // Verificar que la relación cuota/ingreso sea aceptable (máximo 30%)
        if (solicitud.getRelacionCuotaIngreso().compareTo(new BigDecimal("30.0")) > 0) {
            solicitud.setEstado("Rechazada");
            return this.solicitudCreditoRepository.save(solicitud);
        }
        
        // Si pasa las validaciones automáticas, pasa a estado EnRevision para análisis manual
        solicitud.setEstado("EnRevision");
        return this.solicitudCreditoRepository.save(solicitud);
    }
    
    private void calcularValoresFinancieros(SolicitudCredito solicitud) {
        // Implementar cálculo de cuota mensual, total a pagar, etc.
        // Este es un cálculo simplificado, en la realidad se usaría
        // una fórmula financiera más compleja
        
        BigDecimal tasaMensual = solicitud.getTasaAnual().divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP)
                                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        
        BigDecimal montoFinanciado = solicitud.getMontoSolicitado().subtract(solicitud.getEntrada());
        BigDecimal plazoMesesBD = new BigDecimal(solicitud.getPlazoMeses());
        
        // Fórmula: cuota = P * r * (1 + r)^n / ((1 + r)^n - 1)
        BigDecimal numerador = tasaMensual.add(BigDecimal.ONE).pow(solicitud.getPlazoMeses());
        BigDecimal denominador = numerador.subtract(BigDecimal.ONE);
        BigDecimal factor = tasaMensual.multiply(numerador).divide(denominador, 10, RoundingMode.HALF_UP);
        
        BigDecimal cuotaMensual = montoFinanciado.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        solicitud.setCuotaMensual(cuotaMensual);
        
        BigDecimal totalPagar = cuotaMensual.multiply(plazoMesesBD).setScale(2, RoundingMode.HALF_UP);
        solicitud.setTotalPagar(totalPagar);
    }
    
    private void validarTransicionEstado(String estadoActual, String nuevoEstado) {
        // Definir transiciones válidas de estados
        switch (estadoActual) {
            case "Borrador":
                if (!("EnRevision".equals(nuevoEstado) || "Cancelada".equals(nuevoEstado))) {
                    throw new CreditoException("Desde Borrador solo se puede pasar a EnRevision o Cancelada");
                }
                break;
            case "EnRevision":
                if (!("Aprobada".equals(nuevoEstado) || "Rechazada".equals(nuevoEstado) || "Cancelada".equals(nuevoEstado))) {
                    throw new CreditoException("Desde EnRevision solo se puede pasar a Aprobada, Rechazada o Cancelada");
                }
                break;
            case "Aprobada":
            case "Rechazada":
                throw new CreditoException("No se puede cambiar el estado de una solicitud Aprobada o Rechazada");
            case "Cancelada":
                throw new CreditoException("No se puede cambiar el estado de una solicitud Cancelada");
            default:
                throw new CreditoException("Estado no reconocido: " + estadoActual);
        }
    }

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

    @Transactional(readOnly = true)
    public SolicitudCredito getById(Long id) {
        return solicitudCreditoRepository.findById(id)
                .orElseThrow(() -> new SolicitudCreditoNotFoundException(id));
    }
} 