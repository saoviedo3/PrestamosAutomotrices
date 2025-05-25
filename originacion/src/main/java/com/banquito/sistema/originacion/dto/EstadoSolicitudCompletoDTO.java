package com.banquito.sistema.originacion.dto;

import lombok.Data;
import java.util.List;

@Data
public class EstadoSolicitudCompletoDTO {
    private SolicitudCreditoDTO solicitud;
    private ClienteProspectoDTO cliente;
    private List<DocumentoAdjuntoDTO> documentos;
    private ContratoDTO contrato;
    private List<PagareDTO> pagares;
    private boolean documentosCompletos;
    private List<String> tiposDocumentosFaltantes;
} 