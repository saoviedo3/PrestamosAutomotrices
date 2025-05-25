package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrigenacionService {

    private final ClienteProspectoService clienteProspectoService;
    private final SolicitudCreditoService solicitudCreditoService;
    private final DocumentoAdjuntoService documentoAdjuntoService;
    private final TipoDocumentoService tipoDocumentoService;
    private final ContratoService contratoService;
    private final PagareService pagareService;

    /**
     * Proceso completo: Crear cliente y solicitud de crédito
     */
    public SolicitudCredito procesarSolicitudCompleta(ClienteProspecto cliente, SolicitudCredito solicitud) {
        try {
            // 1. Crear o validar cliente
            ClienteProspecto clienteCreado = clienteProspectoService.crear(cliente);
            
            // 2. Asignar cliente a la solicitud
            solicitud.setIdClienteProspecto(clienteCreado.getIdClienteProspecto());
            
            // 3. Crear solicitud de crédito
            SolicitudCredito solicitudCreada = solicitudCreditoService.crear(solicitud);
            
            return solicitudCreada;
            
        } catch (Exception e) {
            throw new BusinessException("Error en el proceso de solicitud completa: " + e.getMessage(), "PROCESO_SOLICITUD_COMPLETA");
        }
    }

    /**
     * Proceso de evaluación de solicitud con documentos
     */
    public void procesarEvaluacionSolicitud(Integer idSolicitud, List<DocumentoAdjunto> documentos) {
        try {
            // 1. Validar que la solicitud exista
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            
            // 2. Adjuntar documentos requeridos
            for (DocumentoAdjunto documento : documentos) {
                documento.setIdSolicitud(idSolicitud);
                documentoAdjuntoService.adjuntarDocumento(documento);
            }
            
            // 3. Validar completitud de documentos
            List<TipoDocumento> tiposRequeridos = tipoDocumentoService.obtenerTiposRequeridos();
            List<Integer> idsRequeridos = tiposRequeridos.stream()
                    .map(TipoDocumento::getIdTipoDocumento)
                    .toList();
            
            boolean documentosCompletos = documentoAdjuntoService.validarDocumentosCompletos(idSolicitud, idsRequeridos);
            
            if (documentosCompletos) {
                // 4. Enviar a revisión si todos los documentos están completos
                solicitudCreditoService.enviarARevision(idSolicitud);
            } else {
                throw new BusinessException("Faltan documentos requeridos para completar la evaluación", "DOCUMENTOS_INCOMPLETOS");
            }
            
        } catch (Exception e) {
            throw new BusinessException("Error en el proceso de evaluación: " + e.getMessage(), "PROCESO_EVALUACION");
        }
    }

    /**
     * Proceso de aprobación y generación de documentos
     */
    public void procesarAprobacion(Integer idSolicitud, String observaciones, String condicionesEspeciales) {
        try {
            // 1. Aprobar solicitud
            SolicitudCredito solicitud = solicitudCreditoService.aprobar(idSolicitud, observaciones);
            
            // 2. Generar contrato
            Contrato contrato = contratoService.generarContrato(idSolicitud, condicionesEspeciales);
            
            // 3. Generar pagarés
            List<Pagare> pagares = pagareService.generarPagaresPorSolicitud(idSolicitud);
            
        } catch (Exception e) {
            throw new BusinessException("Error en el proceso de aprobación: " + e.getMessage(), "PROCESO_APROBACION");
        }
    }

    /**
     * Proceso de firma y activación de contrato
     */
    public void procesarFirmaYActivacion(Integer idSolicitud) {
        try {
            // 1. Buscar contrato por solicitud
            Contrato contrato = contratoService.buscarPorSolicitud(idSolicitud);
            
            // 2. Firmar contrato
            contratoService.firmarContrato(contrato.getIdContrato());
            
            // 3. Activar contrato (esto también marca la solicitud como desembolsada)
            contratoService.activarContrato(contrato.getIdContrato());
            
        } catch (Exception e) {
            throw new BusinessException("Error en el proceso de firma y activación: " + e.getMessage(), "PROCESO_FIRMA_ACTIVACION");
        }
    }

    /**
     * Consultar estado completo de una solicitud
     */
    @Transactional(readOnly = true)
    public EstadoSolicitudCompleto consultarEstadoCompleto(Integer idSolicitud) {
        try {
            // 1. Obtener solicitud
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            
            // 2. Obtener cliente
            ClienteProspecto cliente = clienteProspectoService.buscarPorId(solicitud.getIdClienteProspecto());
            
            // 3. Obtener documentos
            List<DocumentoAdjunto> documentos = documentoAdjuntoService.listarPorSolicitud(idSolicitud);
            
            // 4. Obtener contrato (si existe)
            Contrato contrato = null;
            try {
                contrato = contratoService.buscarPorSolicitud(idSolicitud);
            } catch (Exception e) {
                // No existe contrato aún
            }
            
            // 5. Obtener pagarés (si existen)
            List<Pagare> pagares = pagareService.listarPorSolicitud(idSolicitud);
            
            // 6. Validar documentos completos
            List<TipoDocumento> tiposRequeridos = tipoDocumentoService.obtenerTiposRequeridos();
            List<Integer> idsRequeridos = tiposRequeridos.stream()
                    .map(TipoDocumento::getIdTipoDocumento)
                    .toList();
            boolean documentosCompletos = documentoAdjuntoService.validarDocumentosCompletos(idSolicitud, idsRequeridos);
            
            return new EstadoSolicitudCompleto(solicitud, cliente, documentos, contrato, pagares, documentosCompletos);
            
        } catch (Exception e) {
            throw new BusinessException("Error al consultar estado completo: " + e.getMessage(), "CONSULTAR_ESTADO_COMPLETO");
        }
    }

    /**
     * Simular préstamo con capacidad de pago
     */
    @Transactional(readOnly = true)
    public SimulacionCredito simularCreditoConCapacidad(Integer idCliente, BigDecimal monto, BigDecimal tasa, Integer plazo) {
        try {
            // 1. Validar capacidad del cliente
            ClienteProspecto cliente = clienteProspectoService.buscarPorId(idCliente);
            BigDecimal capacidadPago = clienteProspectoService.calcularCapacidadPago(idCliente);
            
            // 2. Simular crédito
            SolicitudCredito simulacion = solicitudCreditoService.simularCredito(monto, tasa, plazo);
            
            // 3. Evaluar viabilidad
            boolean esViable = simulacion.getCuotaMensual().compareTo(capacidadPago) <= 0;
            BigDecimal porcentajeCompromiso = simulacion.getCuotaMensual()
                    .divide(capacidadPago, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            
            return new SimulacionCredito(simulacion, capacidadPago, esViable, porcentajeCompromiso);
            
        } catch (Exception e) {
            throw new BusinessException("Error en simulación: " + e.getMessage(), "SIMULAR_CREDITO");
        }
    }

    /**
     * Rechazar solicitud con limpieza de documentos relacionados
     */
    public void rechazarSolicitudCompleta(Integer idSolicitud, String motivo) {
        try {
            // 1. Rechazar solicitud
            solicitudCreditoService.rechazar(idSolicitud, motivo);
            
            // 2. Eliminar documentos adjuntos
            documentoAdjuntoService.eliminarDocumentosPorSolicitud(idSolicitud);
            
            // 3. Eliminar contrato si existe
            try {
                Contrato contrato = contratoService.buscarPorSolicitud(idSolicitud);
                contratoService.anularContrato(contrato.getIdContrato(), "Solicitud rechazada: " + motivo);
            } catch (Exception e) {
                // No existe contrato
            }
            
            // 4. Eliminar pagarés si existen
            if (pagareService.existenPagaresPorSolicitud(idSolicitud)) {
                pagareService.eliminarPagaresPorSolicitud(idSolicitud);
            }
            
        } catch (Exception e) {
            throw new BusinessException("Error al rechazar solicitud completa: " + e.getMessage(), "RECHAZAR_SOLICITUD_COMPLETA");
        }
    }

    /**
     * Validar prerrequisitos para procesar una solicitud
     */
    @Transactional(readOnly = true)
    public ValidacionPrerrequisitos validarPrerrequisitos(Integer idSolicitud) {
        try {
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            ClienteProspecto cliente = clienteProspectoService.buscarPorId(solicitud.getIdClienteProspecto());
            
            boolean clienteActivo = clienteProspectoService.puedesolicitarCredito(solicitud.getIdClienteProspecto());
            boolean tieneCapacidadPago = cliente.getIngresos() != null && cliente.getEgresos() != null;
            
            List<TipoDocumento> tiposRequeridos = tipoDocumentoService.obtenerTiposRequeridos();
            List<Integer> idsRequeridos = tiposRequeridos.stream()
                    .map(TipoDocumento::getIdTipoDocumento)
                    .toList();
            boolean documentosCompletos = documentoAdjuntoService.validarDocumentosCompletos(idSolicitud, idsRequeridos);
            
            boolean puedeGenerarContrato = contratoService.puedeGenerarContrato(idSolicitud);
            
            return new ValidacionPrerrequisitos(clienteActivo, tieneCapacidadPago, documentosCompletos, puedeGenerarContrato);
            
        } catch (Exception e) {
            throw new BusinessException("Error en validación de prerrequisitos: " + e.getMessage(), "VALIDAR_PRERREQUISITOS");
        }
    }

    // Clases de respuesta
    public static class EstadoSolicitudCompleto {
        public final SolicitudCredito solicitud;
        public final ClienteProspecto cliente;
        public final List<DocumentoAdjunto> documentos;
        public final Contrato contrato;
        public final List<Pagare> pagares;
        public final boolean documentosCompletos;

        public EstadoSolicitudCompleto(SolicitudCredito solicitud, ClienteProspecto cliente, 
                                     List<DocumentoAdjunto> documentos, Contrato contrato, 
                                     List<Pagare> pagares, boolean documentosCompletos) {
            this.solicitud = solicitud;
            this.cliente = cliente;
            this.documentos = documentos;
            this.contrato = contrato;
            this.pagares = pagares;
            this.documentosCompletos = documentosCompletos;
        }
    }

    public static class SimulacionCredito {
        public final SolicitudCredito simulacion;
        public final BigDecimal capacidadPago;
        public final boolean esViable;
        public final BigDecimal porcentajeCompromiso;

        public SimulacionCredito(SolicitudCredito simulacion, BigDecimal capacidadPago, 
                               boolean esViable, BigDecimal porcentajeCompromiso) {
            this.simulacion = simulacion;
            this.capacidadPago = capacidadPago;
            this.esViable = esViable;
            this.porcentajeCompromiso = porcentajeCompromiso;
        }
    }

    public static class ValidacionPrerrequisitos {
        public final boolean clienteActivo;
        public final boolean tieneCapacidadPago;
        public final boolean documentosCompletos;
        public final boolean puedeGenerarContrato;

        public ValidacionPrerrequisitos(boolean clienteActivo, boolean tieneCapacidadPago, 
                                      boolean documentosCompletos, boolean puedeGenerarContrato) {
            this.clienteActivo = clienteActivo;
            this.tieneCapacidadPago = tieneCapacidadPago;
            this.documentosCompletos = documentosCompletos;
            this.puedeGenerarContrato = puedeGenerarContrato;
        }
    }
} 