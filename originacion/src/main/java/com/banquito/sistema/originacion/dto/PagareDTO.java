package com.banquito.sistema.originacion.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagareDTO {
    private Integer idPagare;
    private Integer idSolicitud;
    private BigDecimal numeroCuota;
    private LocalDateTime fechaGenerado;
    private String rutaArchivo;
} 