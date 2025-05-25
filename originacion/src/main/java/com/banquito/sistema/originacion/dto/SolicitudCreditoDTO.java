package com.banquito.sistema.originacion.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SolicitudCreditoDTO {
    private Integer idSolicitud;
    private Integer idClienteProspecto;
    private Integer idVendedor;
    private BigDecimal montoSolicitado;
    private Integer plazoMeses;
    private BigDecimal tasaInteres;
    private BigDecimal cuotaMensual;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaAprobacion;
    private String motivo;
    
    // Información adicional para respuestas
    private String nombreCliente;
    private String cedulaCliente;
} 