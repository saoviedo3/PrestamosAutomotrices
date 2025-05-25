package com.banquito.sistema.originacion.mapper;

import com.banquito.sistema.originacion.dto.PagareDTO;
import com.banquito.sistema.originacion.model.Pagare;
import org.springframework.stereotype.Component;

@Component
public class PagareMapper {

    public PagareDTO toDTO(Pagare entity) {
        if (entity == null) return null;
        
        PagareDTO dto = new PagareDTO();
        dto.setIdPagare(entity.getIdPagare());
        dto.setIdSolicitud(entity.getIdSolicitud());
        dto.setNumeroCuota(entity.getNumeroCuota());
        dto.setFechaGenerado(entity.getFechaGenerado());
        dto.setRutaArchivo(entity.getRutaArchivo());
        
        return dto;
    }

    public Pagare toEntity(PagareDTO dto) {
        if (dto == null) return null;
        
        Pagare entity = new Pagare();
        entity.setIdPagare(dto.getIdPagare());
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setNumeroCuota(dto.getNumeroCuota());
        entity.setFechaGenerado(dto.getFechaGenerado());
        entity.setRutaArchivo(dto.getRutaArchivo());
        
        return entity;
    }
} 