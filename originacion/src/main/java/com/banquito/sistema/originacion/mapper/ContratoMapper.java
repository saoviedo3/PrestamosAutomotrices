package com.banquito.sistema.originacion.mapper;

import com.banquito.sistema.originacion.dto.ContratoDTO;
import com.banquito.sistema.originacion.model.Contrato;
import org.springframework.stereotype.Component;

@Component
public class ContratoMapper {

    public ContratoDTO toDTO(Contrato entity) {
        if (entity == null) return null;
        
        ContratoDTO dto = new ContratoDTO();
        dto.setIdContrato(entity.getIdContrato());
        dto.setIdSolicitud(entity.getIdSolicitud());
        dto.setFechaGenerado(entity.getFechaGenerado());
        dto.setFechaFirma(entity.getFechaFirma());
        dto.setEstado(entity.getEstado());
        dto.setRutaArchivo(entity.getRutaArchivo());
        dto.setCondicionEspecial(entity.getCondicionEspecial());
        
        return dto;
    }

    public Contrato toEntity(ContratoDTO dto) {
        if (dto == null) return null;
        
        Contrato entity = new Contrato();
        entity.setIdContrato(dto.getIdContrato());
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setFechaGenerado(dto.getFechaGenerado());
        entity.setFechaFirma(dto.getFechaFirma());
        entity.setEstado(dto.getEstado());
        entity.setRutaArchivo(dto.getRutaArchivo());
        entity.setCondicionEspecial(dto.getCondicionEspecial());
        
        return entity;
    }
} 