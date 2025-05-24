package com.banquito.sistema.originacion.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SolicitudCreditoDTO {

    private Integer idSolicitud;
    private Integer idClienteProspecto;
    private Integer idVendedor;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal montoSolicitado;
    
    private Integer plazoMeses;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal tasaInteres;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal cuotaMensual;
    
    private String estado;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaSolicitud;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaAprobacion;
    
    private String motivo;
    
    // Información del cliente (para consultas)
    private String nombreCliente;
    private String apellidoCliente;
    private String cedulaCliente;
} 