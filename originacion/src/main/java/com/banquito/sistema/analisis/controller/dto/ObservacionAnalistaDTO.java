package com.banquito.sistema.analisis.controller.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ObservacionAnalistaDTO {
    
    private Integer id;
    
    @NotNull(message = "El ID de la solicitud es obligatorio")
    private Integer idSolicitud;
    
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario debe tener máximo 50 caracteres")
    private String usuario;
    
    private LocalDateTime fechaHora;
    
    @NotBlank(message = "La razón de intervención es obligatoria")
    @Size(max = 500, message = "La razón de intervención debe tener máximo 500 caracteres")
    private String razonIntervencion;
} 