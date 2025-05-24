package com.banquito.sistema.originacion.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentoAdjuntoDTO {

    private Integer idDocumento;
    private Integer idSolicitud;
    private Integer idTipoDocumento;
    private String rutaArchivo;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCargado;
    
    // Información del tipo de documento (para consultas)
    private String nombreTipoDocumento;
    private String descripcionTipoDocumento;
} 