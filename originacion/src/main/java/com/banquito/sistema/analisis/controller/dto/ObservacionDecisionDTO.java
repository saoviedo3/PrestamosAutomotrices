package com.banquito.sistema.analisis.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para registrar una decisión del analista sobre una solicitud de crédito
 */
@Data
@NoArgsConstructor
public class ObservacionDecisionDTO {
    
    @Valid
    @NotNull(message = "La observación es obligatoria")
    private ObservacionAnalistaDTO observacion;
    
    @NotBlank(message = "La decisión es obligatoria")
    @Pattern(regexp = "Aprobada|Rechazada", message = "La decisión debe ser 'Aprobada' o 'Rechazada'")
    private String decision;
} 