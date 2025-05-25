package com.banquito.sistema.originacion.controller.mapper;

import org.springframework.stereotype.Component;

import com.banquito.sistema.originacion.controller.dto.HistorialEstadoDTO;
import com.banquito.sistema.originacion.model.HistorialEstado;

@Component
public class HistorialEstadoMapper {

    public HistorialEstadoDTO toDTO(HistorialEstado historialEstado) {
        if (historialEstado == null) {
            return null;
        }

        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.setId(historialEstado.getId());
        dto.setEntidadTipo(historialEstado.getEntidadTipo());
        dto.setEntidadId(historialEstado.getEntidadId());
        dto.setEstadoAnterior(historialEstado.getEstadoAnterior());
        dto.setEstadoNuevo(historialEstado.getEstadoNuevo());
        dto.setMotivo(historialEstado.getMotivo());
        dto.setUsuarioCambio(historialEstado.getUsuarioCambio());
        dto.setFechaCambio(historialEstado.getFechaCambio());
        dto.setObservaciones(historialEstado.getObservaciones());

        return dto;
    }

    public HistorialEstado toEntity(HistorialEstadoDTO dto) {
        if (dto == null) {
            return null;
        }

        HistorialEstado historialEstado = new HistorialEstado();
        historialEstado.setId(dto.getId());
        historialEstado.setEntidadTipo(dto.getEntidadTipo());
        historialEstado.setEntidadId(dto.getEntidadId());
        historialEstado.setEstadoAnterior(dto.getEstadoAnterior());
        historialEstado.setEstadoNuevo(dto.getEstadoNuevo());
        historialEstado.setMotivo(dto.getMotivo());
        historialEstado.setUsuarioCambio(dto.getUsuarioCambio());
        historialEstado.setObservaciones(dto.getObservaciones());

        return historialEstado;
    }
} 