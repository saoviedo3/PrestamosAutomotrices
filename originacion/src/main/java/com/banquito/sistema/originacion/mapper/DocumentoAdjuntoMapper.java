package com.banquito.sistema.originacion.mapper;

import com.banquito.sistema.originacion.dto.DocumentoAdjuntoDTO;
import com.banquito.sistema.originacion.model.DocumentoAdjunto;
import org.springframework.stereotype.Component;

@Component
public class DocumentoAdjuntoMapper {

    public DocumentoAdjuntoDTO toDTO(DocumentoAdjunto entity) {
        if (entity == null) return null;
        
        DocumentoAdjuntoDTO dto = new DocumentoAdjuntoDTO();
        dto.setIdDocumento(entity.getIdDocumento());
        dto.setIdSolicitud(entity.getIdSolicitud());
        dto.setIdTipoDocumento(entity.getIdTipoDocumento());
        dto.setRutaArchivo(entity.getRutaArchivo());
        dto.setFechaCargado(entity.getFechaCargado());
        
        return dto;
    }

    public DocumentoAdjunto toEntity(DocumentoAdjuntoDTO dto) {
        if (dto == null) return null;
        
        DocumentoAdjunto entity = new DocumentoAdjunto();
        entity.setIdDocumento(dto.getIdDocumento());
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setIdTipoDocumento(dto.getIdTipoDocumento());
        entity.setRutaArchivo(dto.getRutaArchivo());
        entity.setFechaCargado(dto.getFechaCargado());
        
        return entity;
    }
} 