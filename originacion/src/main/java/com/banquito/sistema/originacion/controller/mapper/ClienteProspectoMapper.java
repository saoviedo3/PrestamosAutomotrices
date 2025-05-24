package com.banquito.sistema.originacion.controller.mapper;

import com.banquito.sistema.originacion.controller.dto.ClienteProspectoDTO;
import com.banquito.sistema.originacion.model.ClienteProspecto;
import org.springframework.stereotype.Component;

@Component
public class ClienteProspectoMapper {

    public ClienteProspectoDTO toDTO(ClienteProspecto clienteProspecto) {
        if (clienteProspecto == null) {
            return null;
        }

        ClienteProspectoDTO dto = new ClienteProspectoDTO();
        dto.setIdClienteProspecto(clienteProspecto.getIdClienteProspecto());
        dto.setCedula(clienteProspecto.getCedula());
        dto.setNombre(clienteProspecto.getNombre());
        dto.setApellido(clienteProspecto.getApellido());
        dto.setTelefono(clienteProspecto.getTelefono());
        dto.setEmail(clienteProspecto.getEmail());
        dto.setDireccion(clienteProspecto.getDireccion());
        dto.setIngresos(clienteProspecto.getIngresos());
        dto.setEgresos(clienteProspecto.getEgresos());
        dto.setActividadEconomica(clienteProspecto.getActividadEconomica());
        dto.setEstado(clienteProspecto.getEstado());

        return dto;
    }

    public ClienteProspecto toEntity(ClienteProspectoDTO dto) {
        if (dto == null) {
            return null;
        }

        ClienteProspecto entity = new ClienteProspecto();
        entity.setIdClienteProspecto(dto.getIdClienteProspecto());
        entity.setCedula(dto.getCedula());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setTelefono(dto.getTelefono());
        entity.setEmail(dto.getEmail());
        entity.setDireccion(dto.getDireccion());
        entity.setIngresos(dto.getIngresos());
        entity.setEgresos(dto.getEgresos());
        entity.setActividadEconomica(dto.getActividadEconomica());
        entity.setEstado(dto.getEstado());

        return entity;
    }
} 