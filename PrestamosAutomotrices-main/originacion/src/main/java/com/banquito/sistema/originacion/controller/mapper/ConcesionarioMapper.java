package com.banquito.sistema.originacion.controller.mapper;

import org.springframework.stereotype.Component;

import com.banquito.sistema.originacion.controller.dto.ConcesionarioDTO;
import com.banquito.sistema.originacion.model.Concesionario;

@Component
public class ConcesionarioMapper {

    public ConcesionarioDTO toDTO(Concesionario concesionario) {
        if (concesionario == null) {
            return null;
        }

        ConcesionarioDTO dto = new ConcesionarioDTO();
        dto.setId(concesionario.getId());
        dto.setCodigo(concesionario.getCodigo());
        dto.setNombre(concesionario.getNombre());
        dto.setDireccion(concesionario.getDireccion());
        dto.setTelefono(concesionario.getTelefono());
        dto.setEmail(concesionario.getEmail());
        dto.setCiudad(concesionario.getCiudad());
        dto.setProvincia(concesionario.getProvincia());
        dto.setEstado(concesionario.getEstado());
        dto.setFechaCreacion(concesionario.getFechaCreacion());
        dto.setFechaActualizacion(concesionario.getFechaActualizacion());

        return dto;
    }

    public Concesionario toEntity(ConcesionarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Concesionario concesionario = new Concesionario();
        concesionario.setId(dto.getId());
        concesionario.setCodigo(dto.getCodigo());
        concesionario.setNombre(dto.getNombre());
        concesionario.setDireccion(dto.getDireccion());
        concesionario.setTelefono(dto.getTelefono());
        concesionario.setEmail(dto.getEmail());
        concesionario.setCiudad(dto.getCiudad());
        concesionario.setProvincia(dto.getProvincia());
        concesionario.setEstado(dto.getEstado());

        return concesionario;
    }

    public void updateEntity(ConcesionarioDTO dto, Concesionario concesionario) {
        if (dto == null || concesionario == null) {
            return;
        }

        concesionario.setCodigo(dto.getCodigo());
        concesionario.setNombre(dto.getNombre());
        concesionario.setDireccion(dto.getDireccion());
        concesionario.setTelefono(dto.getTelefono());
        concesionario.setEmail(dto.getEmail());
        concesionario.setCiudad(dto.getCiudad());
        concesionario.setProvincia(dto.getProvincia());
        concesionario.setEstado(dto.getEstado());
    }
} 