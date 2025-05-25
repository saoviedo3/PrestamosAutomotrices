package com.banquito.sistema.originacion.controller.mapper;

import org.springframework.stereotype.Component;

import com.banquito.sistema.originacion.controller.dto.VendedorDTO;
import com.banquito.sistema.originacion.model.Concesionario;
import com.banquito.sistema.originacion.model.Vendedor;

@Component
public class VendedorMapper {

    public VendedorDTO toDTO(Vendedor vendedor) {
        if (vendedor == null) {
            return null;
        }

        VendedorDTO dto = new VendedorDTO();
        dto.setId(vendedor.getId());
        dto.setCodigo(vendedor.getCodigo());
        dto.setCedula(vendedor.getCedula());
        dto.setNombres(vendedor.getNombres());
        dto.setApellidos(vendedor.getApellidos());
        dto.setTelefono(vendedor.getTelefono());
        dto.setEmail(vendedor.getEmail());
        dto.setEstado(vendedor.getEstado());
        dto.setFechaIngreso(vendedor.getFechaIngreso());
        dto.setFechaCreacion(vendedor.getFechaCreacion());
        dto.setFechaActualizacion(vendedor.getFechaActualizacion());
        
        if (vendedor.getConcesionario() != null) {
            dto.setConcesionarioId(vendedor.getConcesionario().getId());
            dto.setConcesionarioNombre(vendedor.getConcesionario().getNombre());
        }

        return dto;
    }

    public Vendedor toEntity(VendedorDTO dto) {
        if (dto == null) {
            return null;
        }

        Vendedor vendedor = new Vendedor();
        vendedor.setId(dto.getId());
        vendedor.setCodigo(dto.getCodigo());
        vendedor.setCedula(dto.getCedula());
        vendedor.setNombres(dto.getNombres());
        vendedor.setApellidos(dto.getApellidos());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setEmail(dto.getEmail());
        vendedor.setEstado(dto.getEstado());
        vendedor.setFechaIngreso(dto.getFechaIngreso());
        
        if (dto.getConcesionarioId() != null) {
            Concesionario concesionario = new Concesionario(dto.getConcesionarioId());
            vendedor.setConcesionario(concesionario);
        }

        return vendedor;
    }

    public void updateEntity(VendedorDTO dto, Vendedor vendedor) {
        if (dto == null || vendedor == null) {
            return;
        }

        vendedor.setCodigo(dto.getCodigo());
        vendedor.setCedula(dto.getCedula());
        vendedor.setNombres(dto.getNombres());
        vendedor.setApellidos(dto.getApellidos());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setEmail(dto.getEmail());
        vendedor.setEstado(dto.getEstado());
        vendedor.setFechaIngreso(dto.getFechaIngreso());
        
        if (dto.getConcesionarioId() != null) {
            Concesionario concesionario = new Concesionario(dto.getConcesionarioId());
            vendedor.setConcesionario(concesionario);
        }
    }
} 