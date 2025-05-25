package com.banquito.sistema.originacion.mapper;

import com.banquito.sistema.originacion.dto.SolicitudCreditoDTO;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import org.springframework.stereotype.Component;

@Component
public class SolicitudCreditoMapper {

    public SolicitudCreditoDTO toDTO(SolicitudCredito entity) {
        if (entity == null) return null;
        
        SolicitudCreditoDTO dto = new SolicitudCreditoDTO();
        dto.setIdSolicitud(entity.getIdSolicitud());
        dto.setIdClienteProspecto(entity.getIdClienteProspecto());
        dto.setIdVendedor(entity.getIdVendedor());
        dto.setMontoSolicitado(entity.getMontoSolicitado());
        dto.setPlazoMeses(entity.getPlazoMeses());
        dto.setTasaInteres(entity.getTasaInteres());
        dto.setCuotaMensual(entity.getCuotaMensual());
        dto.setEstado(entity.getEstado());
        dto.setFechaSolicitud(entity.getFechaSolicitud());
        dto.setFechaAprobacion(entity.getFechaAprobacion());
        dto.setMotivo(entity.getMotivo());
        
        return dto;
    }

    public SolicitudCredito toEntity(SolicitudCreditoDTO dto) {
        if (dto == null) return null;
        
        SolicitudCredito entity = new SolicitudCredito();
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setIdClienteProspecto(dto.getIdClienteProspecto());
        entity.setIdVendedor(dto.getIdVendedor());
        entity.setMontoSolicitado(dto.getMontoSolicitado());
        entity.setPlazoMeses(dto.getPlazoMeses());
        entity.setTasaInteres(dto.getTasaInteres());
        entity.setCuotaMensual(dto.getCuotaMensual());
        entity.setEstado(dto.getEstado());
        entity.setFechaSolicitud(dto.getFechaSolicitud());
        entity.setFechaAprobacion(dto.getFechaAprobacion());
        entity.setMotivo(dto.getMotivo());
        
        return entity;
    }
} 