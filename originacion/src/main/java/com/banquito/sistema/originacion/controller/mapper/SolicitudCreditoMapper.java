package com.banquito.sistema.originacion.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.sistema.originacion.controller.dto.SolicitudCreditoDTO;
import com.banquito.sistema.originacion.model.SolicitudCredito;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SolicitudCreditoMapper {

    SolicitudCreditoDTO toDTO(SolicitudCredito model);
    
    SolicitudCredito toModel(SolicitudCreditoDTO dto);
} 