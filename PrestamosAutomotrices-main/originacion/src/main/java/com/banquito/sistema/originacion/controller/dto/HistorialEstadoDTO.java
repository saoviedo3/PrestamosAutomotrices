package com.banquito.sistema.originacion.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistorialEstadoDTO {

    private Long id;

    @NotBlank(message = "El tipo de entidad es obligatorio")
    @Size(max = 50, message = "El tipo de entidad no puede tener más de 50 caracteres")
    private String entidadTipo;

    @NotNull(message = "El ID de la entidad es obligatorio")
    private Long entidadId;

    @Size(max = 50, message = "El estado anterior no puede tener más de 50 caracteres")
    private String estadoAnterior;

    @NotBlank(message = "El estado nuevo es obligatorio")
    @Size(max = 50, message = "El estado nuevo no puede tener más de 50 caracteres")
    private String estadoNuevo;

    @Size(max = 500, message = "El motivo no puede tener más de 500 caracteres")
    private String motivo;

    @NotBlank(message = "El usuario que realiza el cambio es obligatorio")
    @Size(max = 100, message = "El usuario no puede tener más de 100 caracteres")
    private String usuarioCambio;

    private LocalDateTime fechaCambio;

    @Size(max = 1000, message = "Las observaciones no pueden tener más de 1000 caracteres")
    private String observaciones;
} 