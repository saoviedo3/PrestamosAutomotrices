package com.banquito.sistema.originacion.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentoAdjuntoDTO {
    private Integer idDocumento;
    private Integer idSolicitud;
    private Integer idTipoDocumento;
    private String rutaArchivo;
    private LocalDateTime fechaCargado;
    
    // Información adicional para respuestas
    private String nombreTipoDocumento;
    private String descripcionTipoDocumento;
} 