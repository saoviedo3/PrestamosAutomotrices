package com.banquito.sistema.contratos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.contratos.model.Contrato;
import com.banquito.sistema.contratos.model.DocumentoAdjunto;
import com.banquito.sistema.contratos.model.Pagare;
import com.banquito.sistema.contratos.model.TipoDocumento;
import com.banquito.sistema.contratos.service.PagareService.CuotaAmortizacion;
import com.banquito.sistema.originacion.model.SolicitudCredito;

/**
 * Servicio principal de integración del módulo de contratos
 * Actúa como facade para el módulo de originación y coordina todas las operaciones
 */
@Service
public class ContratosIntegracionService {

    private final ContratoService contratoService;
    private final PagareService pagareService;
    private final DocumentoAdjuntoService documentoAdjuntoService;
    private final TipoDocumentoService tipoDocumentoService;

    public ContratosIntegracionService(ContratoService contratoService,
                                     PagareService pagareService,
                                     DocumentoAdjuntoService documentoAdjuntoService,
                                     TipoDocumentoService tipoDocumentoService) {
        this.contratoService = contratoService;
        this.pagareService = pagareService;
        this.documentoAdjuntoService = documentoAdjuntoService;
        this.tipoDocumentoService = tipoDocumentoService;
    }

    /**
     * PROCESO PRINCIPAL: Genera contrato y pagarés automáticamente cuando una solicitud es aprobada
     * Este método es llamado desde el módulo de originación
     */
    @Transactional
    public ResultadoProcesamiento procesarSolicitudAprobada(SolicitudCredito solicitudAprobada) {
        ResultadoProcesamiento resultado = new ResultadoProcesamiento();
        resultado.setSolicitudId(solicitudAprobada.getId());

        try {
            // 1. Validar que todos los documentos obligatorios estén adjuntos
            if (!this.documentoAdjuntoService.validarDocumentosObligatorios(solicitudAprobada.getId())) {
                List<TipoDocumento> faltantes = this.documentoAdjuntoService.getDocumentosFaltantes(solicitudAprobada.getId());
                resultado.setExitoso(false);
                resultado.setMensaje("Faltan documentos obligatorios: " + 
                    faltantes.stream().map(TipoDocumento::getNombre).toList());
                return resultado;
            }

            // 2. Generar contrato automáticamente
            Contrato contrato = this.contratoService.generarContratoAutomatico(solicitudAprobada);
            resultado.setContrato(contrato);

            // 3. Los pagarés ya se generan automáticamente en el ContratoService
            List<Pagare> pagares = this.pagareService.findBySolicitudId(solicitudAprobada.getId());
            resultado.setPagares(pagares);

            // 4. Generar tabla de amortización para referencia
            List<CuotaAmortizacion> tablaAmortizacion = this.pagareService.generarTablaAmortizacion(solicitudAprobada);
            resultado.setTablaAmortizacion(tablaAmortizacion);

            resultado.setExitoso(true);
            resultado.setMensaje("Contrato y pagarés generados exitosamente. Total de pagarés: " + pagares.size());

        } catch (Exception e) {
            resultado.setExitoso(false);
            resultado.setMensaje("Error al procesar solicitud: " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Valida si una solicitud está lista para generar contrato
     */
    public boolean estaListaParaContrato(Long idSolicitud) {
        return this.documentoAdjuntoService.validarDocumentosObligatorios(idSolicitud);
    }

    /**
     * Obtiene el estado completo del proceso contractual para una solicitud
     */
    public EstadoContractual getEstadoContractual(Long idSolicitud) {
        EstadoContractual estado = new EstadoContractual();
        estado.setSolicitudId(idSolicitud);

        // Validar documentos
        estado.setDocumentosCompletos(this.documentoAdjuntoService.validarDocumentosObligatorios(idSolicitud));
        estado.setDocumentosFaltantes(this.documentoAdjuntoService.getDocumentosFaltantes(idSolicitud));
        estado.setTotalDocumentosAdjuntos(this.documentoAdjuntoService.contarDocumentosPorSolicitud(idSolicitud));

        // Verificar contrato
        Contrato contrato = this.contratoService.findContratoActivoPorSolicitud(idSolicitud);
        estado.setTieneContrato(contrato != null);
        if (contrato != null) {
            estado.setContratoId(contrato.getId());
            estado.setEstadoContrato(contrato.getEstado());
            estado.setContratoFirmado("Firmado".equals(contrato.getEstado()));
        }

        // Verificar pagarés
        estado.setTienePagares(this.pagareService.existenPagaresPorSolicitud(idSolicitud));
        if (estado.isTienePagares()) {
            List<Pagare> pagares = this.pagareService.findBySolicitudId(idSolicitud);
            estado.setTotalPagares(pagares.size());
        }

        return estado;
    }

    /**
     * Calcula la tabla de amortización para una solicitud (útil para simulaciones)
     */
    public List<CuotaAmortizacion> calcularTablaAmortizacion(SolicitudCredito solicitud) {
        return this.pagareService.generarTablaAmortizacion(solicitud);
    }

    /**
     * Notifica al banco central sobre un contrato firmado
     */
    @Transactional
    public void notificarContratoFirmadoAlBanco(Long contratoId) {
        this.contratoService.notificarContratoAlBanco(contratoId);
    }

    /**
     * Inicializa el sistema con los tipos de documentos básicos
     */
    @Transactional
    public void inicializarSistema() {
        this.tipoDocumentoService.inicializarTiposDocumentosBasicos();
    }

    /**
     * Clase para el resultado del procesamiento
     */
    public static class ResultadoProcesamiento {
        private Long solicitudId;
        private boolean exitoso;
        private String mensaje;
        private Contrato contrato;
        private List<Pagare> pagares;
        private List<CuotaAmortizacion> tablaAmortizacion;

        // Getters y setters
        public Long getSolicitudId() { return solicitudId; }
        public void setSolicitudId(Long solicitudId) { this.solicitudId = solicitudId; }

        public boolean isExitoso() { return exitoso; }
        public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }

        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }

        public Contrato getContrato() { return contrato; }
        public void setContrato(Contrato contrato) { this.contrato = contrato; }

        public List<Pagare> getPagares() { return pagares; }
        public void setPagares(List<Pagare> pagares) { this.pagares = pagares; }

        public List<CuotaAmortizacion> getTablaAmortizacion() { return tablaAmortizacion; }
        public void setTablaAmortizacion(List<CuotaAmortizacion> tablaAmortizacion) { 
            this.tablaAmortizacion = tablaAmortizacion; 
        }
    }

    /**
     * Clase para representar el estado contractual completo
     */
    public static class EstadoContractual {
        private Long solicitudId;
        private boolean documentosCompletos;
        private List<TipoDocumento> documentosFaltantes;
        private Long totalDocumentosAdjuntos;
        private boolean tieneContrato;
        private Long contratoId;
        private String estadoContrato;
        private boolean contratoFirmado;
        private boolean tienePagares;
        private Integer totalPagares;

        // Getters y setters
        public Long getSolicitudId() { return solicitudId; }
        public void setSolicitudId(Long solicitudId) { this.solicitudId = solicitudId; }

        public boolean isDocumentosCompletos() { return documentosCompletos; }
        public void setDocumentosCompletos(boolean documentosCompletos) { 
            this.documentosCompletos = documentosCompletos; 
        }

        public List<TipoDocumento> getDocumentosFaltantes() { return documentosFaltantes; }
        public void setDocumentosFaltantes(List<TipoDocumento> documentosFaltantes) { 
            this.documentosFaltantes = documentosFaltantes; 
        }

        public Long getTotalDocumentosAdjuntos() { return totalDocumentosAdjuntos; }
        public void setTotalDocumentosAdjuntos(Long totalDocumentosAdjuntos) { 
            this.totalDocumentosAdjuntos = totalDocumentosAdjuntos; 
        }

        public boolean isTieneContrato() { return tieneContrato; }
        public void setTieneContrato(boolean tieneContrato) { this.tieneContrato = tieneContrato; }

        public Long getContratoId() { return contratoId; }
        public void setContratoId(Long contratoId) { this.contratoId = contratoId; }

        public String getEstadoContrato() { return estadoContrato; }
        public void setEstadoContrato(String estadoContrato) { this.estadoContrato = estadoContrato; }

        public boolean isContratoFirmado() { return contratoFirmado; }
        public void setContratoFirmado(boolean contratoFirmado) { this.contratoFirmado = contratoFirmado; }

        public boolean isTienePagares() { return tienePagares; }
        public void setTienePagares(boolean tienePagares) { this.tienePagares = tienePagares; }

        public Integer getTotalPagares() { return totalPagares; }
        public void setTotalPagares(Integer totalPagares) { this.totalPagares = totalPagares; }
    }
} 