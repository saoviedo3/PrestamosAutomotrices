package com.banquito.sistema.originacion.mapper;

import com.banquito.sistema.originacion.dto.*;
import com.banquito.sistema.originacion.service.OrigenacionService.EstadoSolicitudCompleto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EstadoSolicitudCompletoMapper {

    private final SolicitudCreditoMapper solicitudCreditoMapper;
    private final ClienteProspectoMapper clienteProspectoMapper;
    private final DocumentoAdjuntoMapper documentoAdjuntoMapper;
    private final ContratoMapper contratoMapper;
    private final PagareMapper pagareMapper;

    public EstadoSolicitudCompletoDTO toDTO(EstadoSolicitudCompleto estado) {
        if (estado == null) return null;
        
        EstadoSolicitudCompletoDTO dto = new EstadoSolicitudCompletoDTO();
        dto.setSolicitud(solicitudCreditoMapper.toDTO(estado.solicitud));
        dto.setCliente(clienteProspectoMapper.toDTO(estado.cliente));
        
        List<DocumentoAdjuntoDTO> documentosDTO = estado.documentos.stream()
                .map(documentoAdjuntoMapper::toDTO)
                .collect(Collectors.toList());
        dto.setDocumentos(documentosDTO);
        
        dto.setContrato(contratoMapper.toDTO(estado.contrato));
        
        List<PagareDTO> pagaresDTO = estado.pagares.stream()
                .map(pagareMapper::toDTO)
                .collect(Collectors.toList());
        dto.setPagares(pagaresDTO);
        
        dto.setDocumentosCompletos(estado.documentosCompletos);
        
        return dto;
    }
} 