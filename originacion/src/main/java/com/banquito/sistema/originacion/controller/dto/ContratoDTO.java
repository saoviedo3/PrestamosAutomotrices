package com.banquito.sistema.originacion.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContratoDTO {

    private Integer idContrato;
    private Integer idSolicitud;
    private String rutaArchivo;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaGenerado;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaFirma;
    
    private String estado;
    private String condicionEspecial;
} 