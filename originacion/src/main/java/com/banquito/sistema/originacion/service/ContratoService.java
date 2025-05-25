package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.repository.ContratoRepository;
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
    private final SolicitudCreditoService solicitudCreditoService;

    // Estados de contrato
    private static final String ESTADO_GENERADO = "GENERADO";
    private static final String ESTADO_FIRMADO = "FIRMADO";
    private static final String ESTADO_VIGENTE = "VIGENTE";
    private static final String ESTADO_ANULADO = "ANULADO";
    private static final String ESTADO_VENCIDO = "VENCIDO";

    // Estados de solicitud requeridos
    private static final String SOLICITUD_APROBADA = "Aprobada";

    /**
     * Generar contrato desde solicitud aprobada
     */
    public Contrato generarContrato(Integer idSolicitud, String condicionEspecial) {
        try {
            // Validar que la solicitud esté aprobada
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            
            if (!SOLICITUD_APROBADA.equals(solicitud.getEstado())) {
                throw new BusinessException("Solo se pueden generar contratos para solicitudes aprobadas", "GENERAR_CONTRATO");
            }
            
            // Verificar que no exista ya un contrato para esta solicitud
            if (contratoRepository.existsBySolicitudCredito_IdSolicitud(idSolicitud)) {
                throw new BusinessException("Ya existe un contrato para esta solicitud", "GENERAR_CONTRATO");
            }
            
            Contrato contrato = new Contrato();
            contrato.setSolicitudCredito(solicitud);
            contrato.setFechaGenerado(LocalDateTime.now());
            contrato.setFechaFirma(LocalDateTime.now()); // Inicializamos con la fecha actual
            contrato.setEstado(ESTADO_GENERADO);
            contrato.setCondicionEspecial(condicionEspecial);
            
            // Generar ruta del archivo (en un caso real se generaría el PDF)
            String rutaArchivo = generarRutaArchivo(idSolicitud);
            contrato.setRutaArchivo(rutaArchivo);
            
            return contratoRepository.save(contrato);
        } catch (Exception e) {
            throw new BusinessException("Error al generar contrato: " + e.getMessage(), "GENERAR_CONTRATO");
        }
    }

    /**
     * Firmar contrato
     */
    public Contrato firmarContrato(Integer idContrato) {
        try {
            Contrato contrato = buscarPorId(idContrato);
            
            if (!ESTADO_GENERADO.equals(contrato.getEstado())) {
                throw new BusinessException("Solo se pueden firmar contratos en estado generado", "FIRMAR_CONTRATO");
            }
            
            contrato.setEstado(ESTADO_FIRMADO);
            contrato.setFechaFirma(LocalDateTime.now());
            
            return contratoRepository.save(contrato);
        } catch (Exception e) {
            throw new BusinessException("Error al firmar contrato: " + e.getMessage(), "FIRMAR_CONTRATO");
        }
    }

    /**
     * Activar contrato (ponerlo vigente)
     */
    public Contrato activarContrato(Integer idContrato) {
        try {
            Contrato contrato = buscarPorId(idContrato);
            
            if (!ESTADO_FIRMADO.equals(contrato.getEstado())) {
                throw new BusinessException("Solo se pueden activar contratos firmados", "ACTIVAR_CONTRATO");
            }
            
            contrato.setEstado(ESTADO_VIGENTE);
            
            // Marcar la solicitud como desembolsada
            solicitudCreditoService.marcarDesembolsada(contrato.getIdSolicitud());
            
            return contratoRepository.save(contrato);
        } catch (Exception e) {
            throw new BusinessException("Error al activar contrato: " + e.getMessage(), "ACTIVAR_CONTRATO");
        }
    }

    /**
     * Anular contrato
     */
    public Contrato anularContrato(Integer idContrato, String motivo) {
        try {
            Contrato contrato = buscarPorId(idContrato);
            
            if (ESTADO_ANULADO.equals(contrato.getEstado()) || ESTADO_VENCIDO.equals(contrato.getEstado())) {
                throw new BusinessException("No se puede anular un contrato ya anulado o vencido", "ANULAR_CONTRATO");
            }
            
            if (motivo == null || motivo.trim().isEmpty()) {
                throw new ValidationException("motivo", "requerido para anular contrato");
            }
            
            contrato.setEstado(ESTADO_ANULADO);
            contrato.setCondicionEspecial(motivo);
            
            return contratoRepository.save(contrato);
        } catch (Exception e) {
            throw new BusinessException("Error al anular contrato: " + e.getMessage(), "ANULAR_CONTRATO");
        }
    }

    /**
     * Actualizar condiciones especiales del contrato
     */
    public Contrato actualizarCondiciones(Integer idContrato, String nuevasCondiciones) {
        try {
            Contrato contrato = buscarPorId(idContrato);
            
            if (ESTADO_VIGENTE.equals(contrato.getEstado())) {
                throw new BusinessException("No se pueden modificar contratos vigentes", "ACTUALIZAR_CONDICIONES");
            }
            
            if (ESTADO_ANULADO.equals(contrato.getEstado()) || ESTADO_VENCIDO.equals(contrato.getEstado())) {
                throw new BusinessException("No se pueden modificar contratos anulados o vencidos", "ACTUALIZAR_CONDICIONES");
            }
            
            contrato.setCondicionEspecial(nuevasCondiciones);
            return contratoRepository.save(contrato);
        } catch (Exception e) {
            throw new BusinessException("Error al actualizar condiciones: " + e.getMessage(), "ACTUALIZAR_CONDICIONES");
        }
    }

    /**
     * Buscar contrato por ID
     */
    @Transactional(readOnly = true)
    public Contrato buscarPorId(Integer id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "Contrato"));
    }

    /**
     * Buscar contrato por solicitud
     */
    @Transactional(readOnly = true)
    public Contrato buscarPorSolicitud(Integer idSolicitud) {
        return contratoRepository.findBySolicitudCredito_IdSolicitud(idSolicitud)
                .orElseThrow(() -> new NotFoundException("Solicitud: " + idSolicitud, "Contrato"));
    }

    /**
     * Listar contratos por estado
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarPorEstado(String estado) {
        return contratoRepository.findByEstado(estado);
    }

    /**
     * Listar contratos generados (pendientes de firma)
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarPendientesFirma() {
        return contratoRepository.findByEstado(ESTADO_GENERADO);
    }

    /**
     * Listar contratos firmados (pendientes de activación)
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarPendientesActivacion() {
        return contratoRepository.findByEstado(ESTADO_FIRMADO);
    }

    /**
     * Listar contratos vigentes
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarVigentes() {
        return contratoRepository.findByEstado(ESTADO_VIGENTE);
    }

    /**
     * Listar contratos ordenados por fecha de generación
     */
    @Transactional(readOnly = true)
    public List<Contrato> listarPorEstadoOrdenadoPorFecha(String estado) {
        return contratoRepository.findByEstadoOrderByFechaGeneradoDesc(estado);
    }

    /**
     * Verificar si existe contrato para una solicitud
     */
    @Transactional(readOnly = true)
    public boolean existeContratoPorSolicitud(Integer idSolicitud) {
        return contratoRepository.existsBySolicitudCredito_IdSolicitud(idSolicitud);
    }

    /**
     * Obtener información completa del contrato incluyendo datos de la solicitud
     */
    @Transactional(readOnly = true)
    public Contrato obtenerContratoCompleto(Integer idContrato) {
        Contrato contrato = buscarPorId(idContrato);
        
        // Cargar información de la solicitud
        solicitudCreditoService.buscarPorId(contrato.getIdSolicitud());
        
        // En este punto podrías agregar más información relacionada
        return contrato;
    }

    /**
     * Regenerar archivo de contrato
     */
    public Contrato regenerarArchivo(Integer idContrato) {
        try {
            Contrato contrato = buscarPorId(idContrato);
            
            if (ESTADO_VIGENTE.equals(contrato.getEstado())) {
                throw new BusinessException("No se puede regenerar archivo de contrato vigente", "REGENERAR_ARCHIVO");
            }
            
            if (ESTADO_ANULADO.equals(contrato.getEstado()) || ESTADO_VENCIDO.equals(contrato.getEstado())) {
                throw new BusinessException("No se puede regenerar archivo de contrato anulado o vencido", "REGENERAR_ARCHIVO");
            }
            
            // Generar nueva ruta de archivo
            String nuevaRuta = generarRutaArchivo(contrato.getIdSolicitud());
            contrato.setRutaArchivo(nuevaRuta);
            
            return contratoRepository.save(contrato);
        } catch (Exception e) {
            throw new BusinessException("Error al regenerar archivo: " + e.getMessage(), "REGENERAR_ARCHIVO");
        }
    }

    /**
     * Validar que se puede generar contrato
     */
    @Transactional(readOnly = true)
    public boolean puedeGenerarContrato(Integer idSolicitud) {
        try {
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            return SOLICITUD_APROBADA.equals(solicitud.getEstado()) && 
                   !contratoRepository.existsBySolicitudCredito_IdSolicitud(idSolicitud);
        } catch (Exception e) {
            return false;
        }
    }

    // Métodos privados auxiliares
    private String generarRutaArchivo(Integer idSolicitud) {
        // En un caso real, aquí se generaría el archivo PDF del contrato
        // y se retornaría la ruta real del archivo
        LocalDateTime ahora = LocalDateTime.now();
        return String.format("/contratos/%d/contrato_solicitud_%d_%d%02d%02d_%02d%02d%02d.pdf",
                ahora.getYear(),
                idSolicitud,
                ahora.getYear(),
                ahora.getMonthValue(),
                ahora.getDayOfMonth(),
                ahora.getHour(),
                ahora.getMinute(),
                ahora.getSecond());
    }
} 