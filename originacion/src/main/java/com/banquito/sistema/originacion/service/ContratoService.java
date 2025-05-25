package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.ContratoNotFoundException;
import com.banquito.sistema.originacion.exception.SolicitudCreditoNotFoundException;
import com.banquito.sistema.originacion.exception.ClienteProspectoNotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.model.ClienteProspecto;
import com.banquito.sistema.originacion.repository.ContratoRepository;
import com.banquito.sistema.originacion.repository.SolicitudCreditoRepository;
import com.banquito.sistema.originacion.repository.ClienteProspectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final SolicitudCreditoRepository solicitudCreditoRepository;
    private final ClienteProspectoRepository clienteProspectoRepository;

    private static final String ESTADO_SOLICITUD_APROBADA = "APROBADA";
    private static final String ESTADO_CLIENTE_ACTIVO = "ACTIVO";
    private static final String ESTADO_CONTRATO_GENERADO = "GENERADO";
    private static final String ESTADO_CONTRATO_FIRMADO = "FIRMADO";
    private static final String ESTADO_CONTRATO_VIGENTE = "VIGENTE";

    /**
     * Crear un nuevo contrato
     */
    public Contrato createContrato(Contrato contrato) {
        try {
            validarContrato(contrato);
            SolicitudCredito solicitud = validarSolicitudCredito(contrato.getIdSolicitud());
            validarClienteProspecto(solicitud.getIdClienteProspecto());
            validarReglasContrato(contrato);
            
            contrato.setFechaGenerado(LocalDateTime.now());
            contrato.setEstado(ESTADO_CONTRATO_GENERADO);
            
            return contratoRepository.save(contrato);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new BusinessException("Error al crear contrato: " + e.getMessage(), "CREAR_CONTRATO");
        }
    }

    /**
     * Buscar contrato por ID
     */
    @Transactional(readOnly = true)
    public Contrato getContratoById(Integer id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new ContratoNotFoundException(id.toString(), "ID"));
    }

    /**
     * Listar contratos por solicitud de crédito
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarPorSolicitud(Integer idSolicitud) {
        validarSolicitudCredito(idSolicitud);
        return contratoRepository.findBySolicitudCredito_IdSolicitud(idSolicitud);
    }

    /**
     * Buscar contrato por solicitud y estado
     */
    @Transactional(readOnly = true)
    public Contrato buscarPorSolicitudYEstado(Integer idSolicitud, String estado) {
        return contratoRepository.findBySolicitudCredito_IdSolicitudAndEstado(idSolicitud, estado)
                .orElseThrow(() -> new ContratoNotFoundException(
                    "Solicitud: " + idSolicitud + ", Estado: " + estado, 
                    "solicitud y estado"));
    }

    /**
     * Listar contratos por estado
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarPorEstado(String estado) {
        return contratoRepository.findByEstado(estado);
    }

    /**
     * Firmar contrato
     */
    public Contrato firmarContrato(Integer id) {
        Contrato contrato = getContratoById(id);
        
        if (!ESTADO_CONTRATO_GENERADO.equals(contrato.getEstado())) {
            throw new BusinessException("El contrato debe estar en estado GENERADO para ser firmado", "ESTADO_INVALIDO_FIRMA");
        }
        
        contrato.setFechaFirma(LocalDateTime.now());
        contrato.setEstado(ESTADO_CONTRATO_FIRMADO);
        
        return contratoRepository.save(contrato);
    }

    /**
     * Activar contrato (ponerlo vigente)
     */
    public Contrato activarContrato(Integer id) {
        Contrato contrato = getContratoById(id);
        
        if (!ESTADO_CONTRATO_FIRMADO.equals(contrato.getEstado())) {
            throw new BusinessException("El contrato debe estar firmado para ser activado", "ESTADO_INVALIDO_ACTIVACION");
        }
        
        contrato.setEstado(ESTADO_CONTRATO_VIGENTE);
        return contratoRepository.save(contrato);
    }

    /**
     * Verificar si existe contrato para una solicitud
     */
    @Transactional(readOnly = true)
    public boolean existeContrato(Integer idSolicitud) {
        return contratoRepository.existsBySolicitudCredito_IdSolicitud(idSolicitud);
    }

    /**
     * Contar contratos por estado
     */
    @Transactional(readOnly = true)
    public long contarPorEstado(String estado) {
        return contratoRepository.countByEstado(estado);
    }

    /**
     * Listar contratos por rango de fechas de generación
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarPorRangoFechasGeneracion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return contratoRepository.findByFechaGeneradoBetween(fechaInicio, fechaFin);
    }

    /**
     * Listar contratos por rango de fechas de firma
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarPorRangoFechasFirma(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return contratoRepository.findByFechaFirmaBetween(fechaInicio, fechaFin);
    }

    // Métodos privados de validación
    private void validarContrato(Contrato contrato) {
        if (contrato.getIdSolicitud() == null) {
            throw new ValidationException("idSolicitud", "requerido");
        }
        
        if (contrato.getRutaArchivo() == null || contrato.getRutaArchivo().trim().isEmpty()) {
            throw new ValidationException("rutaArchivo", "requerida");
        }
        
        if (contrato.getFechaFirma() == null) {
            throw new ValidationException("fechaFirma", "requerida");
        }
        
        if (contrato.getFechaFirma().isBefore(LocalDateTime.now().minusDays(1))) {
            throw new ValidationException("fechaFirma", "no puede ser anterior a ayer");
        }
    }

    private SolicitudCredito validarSolicitudCredito(Integer idSolicitud) {
        SolicitudCredito solicitud = solicitudCreditoRepository.findById(idSolicitud)
                .orElseThrow(() -> new SolicitudCreditoNotFoundException(idSolicitud.toString(), "ID"));
        
        if (!ESTADO_SOLICITUD_APROBADA.equals(solicitud.getEstado())) {
            throw new BusinessException("La solicitud de crédito debe estar aprobada para generar contratos", "SOLICITUD_NO_APROBADA");
        }
        
        return solicitud;
    }

    private void validarClienteProspecto(Integer idClienteProspecto) {
        ClienteProspecto cliente = clienteProspectoRepository.findById(idClienteProspecto)
                .orElseThrow(() -> new ClienteProspectoNotFoundException(idClienteProspecto.toString(), "ID"));
        
        if (!ESTADO_CLIENTE_ACTIVO.equals(cliente.getEstado())) {
            throw new BusinessException("El cliente debe estar activo para generar contratos", "CLIENTE_NO_ACTIVO");
        }
    }

    private void validarReglasContrato(Contrato contrato) {
        // Verificar que no exista ya un contrato para la misma solicitud
        if (contratoRepository.existsBySolicitudCredito_IdSolicitud(contrato.getIdSolicitud())) {
            throw new BusinessException(
                "Ya existe un contrato para la solicitud " + contrato.getIdSolicitud(), 
                "CONTRATO_DUPLICADO");
        }
        
        // Verificar que la ruta del archivo no esté duplicada
        if (contratoRepository.existsByRutaArchivo(contrato.getRutaArchivo())) {
            throw new BusinessException("La ruta del archivo ya está en uso", "RUTA_ARCHIVO_DUPLICADA");
        }
    }
} 