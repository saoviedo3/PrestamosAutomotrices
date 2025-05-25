package com.banquito.sistema.originacion.mapper;

import com.banquito.sistema.originacion.dto.TipoDocumentoDTO;
import com.banquito.sistema.originacion.model.TipoDocumento;
import org.springframework.stereotype.Component;

@Component
public class TipoDocumentoMapper {

    public TipoDocumentoDTO toDTO(TipoDocumento entity) {
        if (entity == null) return null;
        
        TipoDocumentoDTO dto = new TipoDocumentoDTO();
        dto.setIdTipoDocumento(entity.getIdTipoDocumento());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setEstado(entity.getEstado());
        
        return dto;
    }

    public TipoDocumento toEntity(TipoDocumentoDTO dto) {
        if (dto == null) return null;
        
        TipoDocumento entity = new TipoDocumento();
        entity.setIdTipoDocumento(dto.getIdTipoDocumento());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEstado(dto.getEstado());
        
        return entity;
    }
} 