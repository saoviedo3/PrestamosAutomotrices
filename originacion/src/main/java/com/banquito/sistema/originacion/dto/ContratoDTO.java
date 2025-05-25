package com.banquito.sistema.originacion.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContratoDTO {
    private Integer idContrato;
    private Integer idSolicitud;
    private LocalDateTime fechaGenerado;
    private LocalDateTime fechaFirma;
    private String estado;
    private String rutaArchivo;
    private String condicionEspecial;
    
    // Información adicional para respuestas
    private String estadoSolicitud;
    private String nombreCliente;
} 