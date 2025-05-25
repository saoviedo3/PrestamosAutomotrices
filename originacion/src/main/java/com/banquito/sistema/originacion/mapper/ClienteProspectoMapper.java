package com.banquito.sistema.originacion.mapper;

import com.banquito.sistema.originacion.dto.ClienteProspectoDTO;
import com.banquito.sistema.originacion.model.ClienteProspecto;
import org.springframework.stereotype.Component;

@Component
public class ClienteProspectoMapper {

    public ClienteProspectoDTO toDTO(ClienteProspecto entity) {
        if (entity == null) return null;
        
        ClienteProspectoDTO dto = new ClienteProspectoDTO();
        dto.setIdClienteProspecto(entity.getIdClienteProspecto());
        dto.setCedula(entity.getCedula());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setIngresos(entity.getIngresos());
        dto.setEgresos(entity.getEgresos());
        dto.setEstado(entity.getEstado());
        
        return dto;
    }

    public ClienteProspecto toEntity(ClienteProspectoDTO dto) {
        if (dto == null) return null;
        
        ClienteProspecto entity = new ClienteProspecto();
        entity.setIdClienteProspecto(dto.getIdClienteProspecto());
        entity.setCedula(dto.getCedula());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        entity.setIngresos(dto.getIngresos());
        entity.setEgresos(dto.getEgresos());
        entity.setEstado(dto.getEstado());
        
        return entity;
    }
} 