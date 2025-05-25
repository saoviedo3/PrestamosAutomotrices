package com.banquito.sistema.analisis.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.sistema.analisis.controller.dto.ObservacionAnalistaDTO;
import com.banquito.sistema.analisis.model.ObservacionAnalista;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ObservacionAnalistaMapper {

    ObservacionAnalistaDTO toDTO(ObservacionAnalista model);
    
    ObservacionAnalista toModel(ObservacionAnalistaDTO dto);
} 